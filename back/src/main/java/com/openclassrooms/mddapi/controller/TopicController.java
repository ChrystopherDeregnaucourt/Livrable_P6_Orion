package com.openclassrooms.mddapi.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.payload.response.TopicDto;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.services.UserDetailsImpl;
import com.openclassrooms.mddapi.service.ITopicService;

@RestController
@RequestMapping("/api/topics") // On utilise le pluriel et le préfixe api
public class TopicController {
	
	@Autowired
	private ITopicService topicService;
	
	@Autowired
	private UserRepository userRepository;

	// GET : Récupérer tous les thèmes
	@GetMapping
	@Transactional
	public ResponseEntity<List<TopicDto>> getTopics(Principal principal) {
		// 1. Récupérer l'utilisateur connecté pour vérifier ses abonnements
		User currentUser = null;
		if (principal != null) {
			UserDetailsImpl userDetails = (UserDetailsImpl) ((Authentication) principal).getPrincipal();
			currentUser = userRepository.findById(userDetails.getId()).orElse(null);
		}
		
		final User user = currentUser; // Pour utilisation dans le stream

		// 2. Récupérer tous les topics et mapper en DTO
		List<TopicDto> dtos = topicService.findAll().stream().map(topic -> {
			TopicDto dto = new TopicDto();
			dto.setId(topic.getId());
			dto.setName(topic.getName());
			dto.setDescription(topic.getDescription());
			
			// 3. Vérifier si l'utilisateur est abonné à ce topic
			if (user != null && user.getSubscriptions() != null) {
				boolean isSubscribed = user.getSubscriptions().contains(topic);
				dto.setSubscribed(isSubscribed);
			} else {
				dto.setSubscribed(false);
			}
			
			return dto;
		}).collect(Collectors.toList());

		return ResponseEntity.ok(dtos);
	}
	
	// POST : S'abonner à un thème
	@PostMapping("/{id}/subscribe")
	public ResponseEntity<?> subscribe(@PathVariable Long id, Principal principal) {
		UserDetailsImpl userDetails = (UserDetailsImpl) ((Authentication) principal).getPrincipal();
		User user = userRepository.findById(userDetails.getId()).orElse(null);
		Topic topic = topicService.findById(id);
		
		if (user == null || topic == null) {
			return ResponseEntity.badRequest().build();
		}
		
		// Ajout de l'abonnement
		if (!user.getSubscriptions().contains(topic)) {
			user.getSubscriptions().add(topic);
			userRepository.save(user);
		}
		
		return ResponseEntity.ok().build();
	}
	
	// DELETE : Se désabonner
	@DeleteMapping("/{id}/subscribe")
	public ResponseEntity<?> unsubscribe(@PathVariable Long id, Principal principal) {
		UserDetailsImpl userDetails = (UserDetailsImpl) ((Authentication) principal).getPrincipal();
		User user = userRepository.findById(userDetails.getId()).orElse(null);
		Topic topic = topicService.findById(id);
		
		if (user == null || topic == null) {
			return ResponseEntity.badRequest().build();
		}
		
		// Suppression de l'abonnement
		if (user.getSubscriptions().contains(topic)) {
			user.getSubscriptions().remove(topic);
			userRepository.save(user);
		}
		
		return ResponseEntity.ok().build();
	}
}
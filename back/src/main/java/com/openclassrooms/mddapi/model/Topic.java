package com.openclassrooms.mddapi.model;

import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "topics")
@Data
public class Topic {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "topic_id")
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(columnDefinition = "TEXT")
	private String description;
}
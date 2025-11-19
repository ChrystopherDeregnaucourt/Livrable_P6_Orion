package com.openclassrooms.mddapi.service;

import java.util.List;
import com.openclassrooms.mddapi.model.Topic;

public interface ITopicService {
    List<Topic> findAll();
    Topic findById(Long id);
}
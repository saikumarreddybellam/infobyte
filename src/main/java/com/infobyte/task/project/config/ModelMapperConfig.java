package com.infobyte.task.project.config;

import com.infobyte.task.project.dtos.QuestionDto;
import com.infobyte.task.project.dtos.QuizDto;
import com.infobyte.task.project.entities.Question;
import com.infobyte.task.project.entities.Quiz;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Custom mapping for Quiz to QuizDto
        modelMapper.typeMap(Quiz.class, QuizDto.class).addMappings(mapper -> {
            mapper.map(Quiz::getQuestions, QuizDto::setQuestions);
        });

        // Custom mapping for Question to QuestionDto
        modelMapper.typeMap(Question.class, QuestionDto.class).addMappings(mapper -> {
            mapper.map(Question::getOptions, QuestionDto::setOptions);
        });

        return modelMapper;
    }
}

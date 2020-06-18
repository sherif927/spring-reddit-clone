package io.bitpark.redditclone.services;

import io.bitpark.redditclone.dto.SubRedditDto;
import io.bitpark.redditclone.exceptions.SubRedditNotFoundException;
import io.bitpark.redditclone.mappers.SubRedditMapper;
import io.bitpark.redditclone.models.SubReddit;
import io.bitpark.redditclone.repositories.SubRedditRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubRedditService {

    private final SubRedditRepository subRedditRepository;
    private final SubRedditMapper subRedditMapper;

    public SubRedditDto createSubReddit(SubRedditDto subRedditDto) {
        SubReddit subReddit = subRedditRepository.save(subRedditMapper.mapDtoToSubReddit(subRedditDto));
        subRedditDto.setId(subReddit.getId());
        return subRedditDto;
    }

    public List<SubRedditDto> getAllSubReddits(){
        return subRedditRepository.
                findAll()
                .stream()
                .map(s ->subRedditMapper.mapSubRedditToDto(s))
                .collect(Collectors.toList());
    }

    public SubRedditDto getById(long id) {
        SubRedditDto subReddit = subRedditRepository.findById(id)
                .map(s -> subRedditMapper.mapSubRedditToDto(s))
                .orElseThrow(() -> new SubRedditNotFoundException("The specified SubReddit does not exist"));

        return subReddit;
    }
}

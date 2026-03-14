package com.example.identityfamily.core.domain.parent;

import com.example.identityfamily.core.domain.exception.PhoneNumberAlreadyExist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParentServiceImpl implements ParentService {

    private ParentRepository parentRepository;


    @Override
    public ParentDto addParent(ParentDto parentDto) {
        if(parentRepository.existsByPhoneNumber(parentDto.getPhoneNumber())){
            throw new PhoneNumberAlreadyExist();
        }
        ParentEntity parentEntity = ParentMapper.mapToEntity(parentDto);
        parentEntity = parentRepository.save(parentEntity);
        return ParentMapper.mapToDto(parentEntity);
    }
}

package com.example.demo.domain.support.foo.converter;

import com.example.demo.domain.dto.request.FooDomainRequest;
import com.example.demo.domain.dto.response.FooDomainResponse;
import com.example.demo.domain.model.Foo;
import com.example.demo.repository.entity.FooEntity;
import org.springframework.stereotype.Component;

@Component
public class FooConverter {

	public Foo from(FooDomainRequest source) {
		return Foo.builder().name(source.getName()).build();
	}

	public FooDomainResponse toDomainResponse(Foo source) {
		return FooDomainResponse.builder().name(source.getName()).build();
	}

	public FooEntity toEntity(Foo source) {
		return FooEntity.builder().build();
	}
}

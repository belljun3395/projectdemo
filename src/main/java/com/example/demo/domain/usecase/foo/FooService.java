package com.example.demo.domain.usecase.foo;

import com.example.demo.domain.dto.request.FooDomainRequest;
import com.example.demo.domain.dto.response.FooDomainResponse;
import com.example.demo.domain.model.Foo;
import com.example.demo.domain.support.foo.converter.FooConverter;
import com.example.demo.domain.util.FooDoSomething;
import com.example.demo.repository.entity.FooEntity;
import com.example.demo.repository.repository.FooRepository;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FooService {

	private final FooRepository fooRepository;
	private final FooConverter fooConverter;
	private final FooDoSomething fooDoSomething;

	@Transactional
	public FooDomainResponse execute(FooDomainRequest request) {
		Foo foo = fooConverter.from(request);

		log.info("======= save foo =======");
		fooRepository.save(fooConverter.toEntity(foo));

		log.info("======= find all foo =======");
		List<FooEntity> all = fooRepository.findAll();

		log.info("======= foo do something =======");
		fooDoSomething.a("a");
		fooDoSomething.b("b");
		fooDoSomething.c("c");

		return fooConverter.toDomainResponse(foo);
	}
}

package dev.changuii.project.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.changuii.project.entity.SampleEntity;
import dev.changuii.project.repository.CustomSampleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import static dev.changuii.project.entity.QSampleEntity.sampleEntity;


public class CustomSampleRepositoryImpl implements CustomSampleRepository {

    private JPAQueryFactory jpaQueryFactory;

    public CustomSampleRepositoryImpl(
            @Autowired JPAQueryFactory jpaQueryFactory
    ){
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public SampleEntity SampleQuery() {
        return jpaQueryFactory
                .select(sampleEntity)
                .from(sampleEntity)
                .fetchOne();
    }
}

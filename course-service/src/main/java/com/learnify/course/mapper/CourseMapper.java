package com.learnify.course.mapper;

import com.learnify.course.dto.request.CreateCourseRequest;
import com.learnify.course.dto.response.CourseResponse;
import com.learnify.course.entity.Course;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "instructorId", ignore = true)
    @Mapping(target = "instructorName", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "averageRating", ignore = true)
    @Mapping(target = "totalReviews", ignore = true)
    @Mapping(target = "totalEnrollments", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "publishedAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    Course toEntity(CreateCourseRequest request);

    @Mapping(target = "categoryName", source = "category.name")
    CourseResponse toResponse(Course course);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCourseFromRequest(CreateCourseRequest request, @MappingTarget Course course);
}
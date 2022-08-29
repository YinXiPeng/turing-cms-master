package com.bonc.turing.cms.exercise.service;



public interface CosFreeCodeService {

    Object createFreeCode(String courseId, Integer num);

    Object showFreeCodeByCourseId(String courseId);
}

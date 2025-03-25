package tech.trvihnls.mobileapis.main.services;

import tech.trvihnls.mobileapis.main.dtos.response.HomeResponse;
import tech.trvihnls.mobileapis.main.dtos.response.ReviewResponse;

public interface MainService {
    HomeResponse retrieveHomeData();
    ReviewResponse retrieveReviewData();
}

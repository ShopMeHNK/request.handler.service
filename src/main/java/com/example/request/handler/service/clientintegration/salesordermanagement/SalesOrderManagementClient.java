package com.example.request.handler.service.clientintegration.salesordermanagement;


import com.example.request.handler.service.dto.common.AdaptorResponse;
import com.example.request.handler.service.dto.customer.ViewCustomerOrderHistoryResponse;
import com.example.request.handler.service.dto.metadata.ItemDetails;
import com.example.request.handler.service.util.constants.Constants;
import com.example.request.handler.service.util.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
public class SalesOrderManagementClient {

    @Autowired
    RestTemplate restTemplate;

    @Value("${vms.view.history.details.url}")
    private String viewHistoryUrl;


    public AdaptorResponse<ViewCustomerOrderHistoryResponse> viewHistory(int pageNumber, int itemsPerPage, String customerId, String status) throws BaseException {
        try{
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(viewHistoryUrl)
                    .queryParam(Constants.ITEMS_PER_PAGE, itemsPerPage)
                    .queryParam(Constants.PAGE_NUMBER, pageNumber)
                    .queryParam(Constants.STATUS, status);
            if(customerId!=null)
                uriComponentsBuilder.queryParam(Constants.CUSTOMER_ID, customerId);

            ParameterizedTypeReference<AdaptorResponse<ViewCustomerOrderHistoryResponse>> typeRef = new ParameterizedTypeReference<AdaptorResponse<ViewCustomerOrderHistoryResponse>>() {
            };
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", MediaType.APPLICATION_JSON.toString());

            return restTemplate.exchange(uriComponentsBuilder.build().toString(), HttpMethod.GET, new HttpEntity<>(headers), typeRef).getBody();

        } catch (Exception ex) {
            throw new BaseException(ex.getMessage(), Constants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, Constants.INTERNAL_SERVER_ERROR_CODE, null);
        }
    }
}

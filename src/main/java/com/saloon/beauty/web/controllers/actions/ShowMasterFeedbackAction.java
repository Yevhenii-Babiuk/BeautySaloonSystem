package com.saloon.beauty.web.controllers.actions;

import com.saloon.beauty.repository.dto.SlotDto;
import com.saloon.beauty.repository.entity.User;
import com.saloon.beauty.services.FeedbackService;
import com.saloon.beauty.services.Service;
import com.saloon.beauty.services.SlotService;
import com.saloon.beauty.web.controllers.PaginationHelper;
import com.saloon.beauty.web.controllers.ServletResources;
import com.saloon.beauty.web.controllers.forms.ActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Action for showing master`s slots with feedback
 */
public class ShowMasterFeedbackAction extends Action {

    private SlotService slotService;

    /**
     * Gets all slots with feedback of master and adds them to request
     * @param request the request need to be processed
     * @param response the response to user
     * @param form - form need to be processed by this action
     * @param resources - servlet's resources
     * @return path to the show master`s feedback page
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ServletResources resources) {

        long masterId = ((User) request
                .getSession()
                .getAttribute("loggedInUser"))
                .getId();
        List<SlotDto> slots = getMasterSlots(masterId, request, slotService, paginationHelper);
        request.setAttribute("slots", slots);
        paginationHelper.addParameterToPagination(request);

        addPaginationToRequest(masterId, request, paginationHelper);
        paginationHelper.addParameterToPagination(request);

        return resources.getForward("ShowMasterFeedbackPage");
    }

    /**
     * Gives a {@code List} with paginated part of all slots
     */
    List<SlotDto> getMasterSlots(long masterId, HttpServletRequest request, SlotService slotService, PaginationHelper paginationHelper) {

        int recordsPerPage = paginationHelper.getRecordsPerPage();
        int previousRecordNumber = paginationHelper.getPreviousRecordNumber(request, recordsPerPage);

        return slotService
                .findSlots(masterId, null, 0L, 0L,
                        null, null, null, null, true, recordsPerPage, previousRecordNumber);
    }

    /**
     * Adds pagination to request
     */
    void addPaginationToRequest(long masterId, HttpServletRequest request, PaginationHelper paginationHelper) {
        long recordsQuantity = slotService.getSlotSearchResultCount(masterId, null, 0L, 0L,
                null, null, null, null, true);
        paginationHelper.addPaginationToRequest(request, recordsQuantity);
    }

    public void setSlotService(Service slotService) {
        this.slotService = (SlotService) slotService;
    }
}

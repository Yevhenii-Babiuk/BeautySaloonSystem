package com.saloon.beauty.web.controllers;


import com.saloon.beauty.web.controllers.actions.Action;
import com.saloon.beauty.web.controllers.config.ActionServletConfigurator;
import com.saloon.beauty.web.controllers.forms.ActionForm;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Front controllers of this web-application. It handles
 * all the requests from clients and redirects them to
 * corresponding {@code Action}.
 * If request has html-form data which is need to be
 * validated {@code ActionServlet} validates it before
 * executing {@code Action}.
 * If form has errors {@code ActionServlet} forwards
 * request to the form page with errors data attached.
 */
public class ActionServlet extends HttpServlet {

    /**
     * Thread-safety servlets resources
     */
    private ServletResources servletResources;

    /**
     * Initializes base {@code HttpServlet} class
     * and {@code servletResources}
     */
    @Override
    public void init(){
        servletResources = new ActionServletConfigurator().createServletResources();
        getServletContext().setAttribute("servletResources", servletResources);
    }

    /**
     * {@inheritDoc}
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response, servletResources);
    }

    /**
     * {@inheritDoc}
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response, servletResources);
    }

    /**
     * Processes all the client's requests
     * @param request - an {@link HttpServletRequest} object that
     *                  contains the request the client has made
     *                  of the servlet
     * @param response - an {@link HttpServletResponse} object that
     *                  contains the response the servlet sends
     *                  to the client
     * @param servletResources - the resources of the servlet
     * @throws IOException - if an input or output error is
     *                              detected when the servlet handles
     *                              the request
     * @throws ServletException - if the request for the POST
     *                                  could not be handled
     */
    void processRequest(HttpServletRequest request, HttpServletResponse response,
                        ServletResources servletResources) throws IOException, ServletException {

        String actionPath = getActionPath(request);
        String redirectPath;

        Action userAction = servletResources.getAction(actionPath);

        if (userAction != null) {
            redirectPath = executeAction(request, response, userAction, actionPath, servletResources);
        } else {
            // If there is no action bound to a current action path
            // trying to find forward path bound with this action path
            redirectPath = servletResources.getForward(actionPath);
        }

        //This condition defines a way to direct the request. It will be forwarded or redirect
        //will be sent to user depending on special prefix existence in {@code redirectPath}
        if (servletResources.isRedirect(redirectPath)) {
            response.sendRedirect(
                    servletResources.getRedirectPath(redirectPath));
        } else {
            request.getRequestDispatcher(redirectPath).forward(request, response);
        }
    }

    /**
     * Executes an {@code Action}. If {@code Action} need validated html-form
     * data run form validation before executing. If form has errors forwards
     * request to the form page with errors data attached.
     * @param userAction - the action need to be executed
     * @param actionPath - path the action is bound with in a {@code servletResources}
     * @param servletResources - the servlet's resources
     * @return - path where request must be forwarded
     */
    String executeAction(HttpServletRequest request, HttpServletResponse response,
                                 Action userAction, String actionPath, ServletResources servletResources) {

        ActionForm form = servletResources.getForm(actionPath);
        String redirectPath;

        ActionErrors errors = fillAndValidateForm(request, form, userAction);

        if (errors.isHasErrors()) {
            request.setAttribute("form", form);
            request.setAttribute("errors", errors);
            redirectPath = userAction.getInputPath();
        } else {
            redirectPath = userAction.execute(request, response, form, servletResources);
        }

        return redirectPath;
    }

    /**
     * Gives a path to the {@code Action} is bound with in
     * a {@code servletResources}.
     * @return a path to the {@code Action}
     */
    String getActionPath(HttpServletRequest request) {
        return request.getServletPath();
    }

    /**
     * Fills and validates given form.
     * If form has no errors or form is {@code null} return
     * {@code ActionErrors} with no errors. Otherwise add all the
     * errors occurred to the {@code ActionErrors} and returns it.
     * @param form - the form need to be filled and validated
     * @param userAction - the current user's action
     * @return - object contains results of validating given form
     */
    ActionErrors fillAndValidateForm(HttpServletRequest request, ActionForm form, Action userAction) {
        ActionErrors errors = new ActionErrors();

        if (form == null) {
            return errors;
        } else {
            form.fill(request);
        }

        if (userAction.isNeedValidate()) {
            errors = form.validate();
        }

        return errors;
    }

}

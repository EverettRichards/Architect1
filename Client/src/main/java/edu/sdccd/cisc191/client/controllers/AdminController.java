package edu.sdccd.cisc191.client.controllers;

import edu.sdccd.cisc191.common.entities.DataFetcher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * UserController*
 * Handles routing and views Admin only pages
 * and operations.
 */

@Controller
public class AdminController implements DataFetcher {
    private final String baseURL = backendEndpointURL + userEndpointURL;

    @GetMapping("/dashboard/admin")
    public String adminDashboard(Model model) {
        return "dashboard-admin";
    }
}

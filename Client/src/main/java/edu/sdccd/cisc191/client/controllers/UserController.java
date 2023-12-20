package edu.sdccd.cisc191.client.controllers;

import edu.sdccd.cisc191.client.errors.InvalidPayloadException;
import edu.sdccd.cisc191.client.errors.UsernameTakenException;
import edu.sdccd.cisc191.client.models.DefaultStocksFileIO;
import edu.sdccd.cisc191.client.models.NewUser;
import edu.sdccd.cisc191.common.cryptography.Hasher;
import edu.sdccd.cisc191.common.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import edu.sdccd.cisc191.common.entities.DataFetcher;
import edu.sdccd.cisc191.client.models.UserDataFetcher;

import java.util.ArrayList;

/**
 * UserController*
 * Handles routing and views for a user sign-in, sign-up,
 * and account options.
 */
@Controller
public class UserController implements DataFetcher {
    private final String baseURL = backendEndpointURL + userEndpointURL;

    /**
     * Renders Sign In Page
     */
    @GetMapping("/sign-in")
    public String signIn() {
        return "signin";
    }

    /**
     * Renders Sign In Success page
     */
    @GetMapping("/sign-in/success")
    public String signInSuccess() {
        return "signin-success";
    }

    /**
     * Renders Sign Up page
     * @param model model to pass an empty user for collecting form data
     * @return signup (sign up page)
     */
    @GetMapping("/sign-up")
    public String signUp(Model model) {
        NewUser newUser = new NewUser();
        model.addAttribute("user", newUser);
        return "signup";
    }

    /**
     * Handles Sign Up form submission
     * @param model model to pass error messages to Sign Up page.
     * @return signup (sign up page if error)
     * @return signup-success (sign up success page)
     */
    @PostMapping("sign-up")
    public String signUpNewUser(@ModelAttribute("user") NewUser newUser, Model model) {
        String passwordHash = Hasher.hashNewPassword(newUser.getPassword());

        //Read from file i/o for a default list of stock tickers to follow.
        DefaultStocksFileIO defaultStocks = new DefaultStocksFileIO();
        defaultStocks.readAndUpdateDefaultStocks();
        ArrayList<String> tickers = defaultStocks.getDefaultStocks();

        User userSignUp = new User(newUser.getEmail(), newUser.getUsername(), newUser.getNickname(), passwordHash, User.Role.Regular, tickers);

        try {
            UserDataFetcher.add(userSignUp);
        } catch (UsernameTakenException error) {
            model.addAttribute("error", "The username is taken. Please try a different username.");
            return "signup";
        } catch (InvalidPayloadException error) {
            model.addAttribute("error", "Something went wrong.");
            return "signup";
        }
        return "signup-success";
    }

    /**
     * Renders account page that shows authenticated user's details.
     * @param model model to pass error messages or user data to account
     *              page.
     * @param request Used for session data to get the authenticated
     *                user's details.
     * @return myaccount (account page)
     */
    @GetMapping("/my-account")
    public String myAccount(Model model, HttpServletRequest request) {
        //For getting user information
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        HttpSession session = request.getSession();

        User user;

        if (session.getAttribute("user") != null) {
            user = (User) session.getAttribute("user");
        } else {
            try {
                user = UserDataFetcher.get(currentPrincipalName);
            } catch (Exception error) {
                model.addAttribute("error", "Something went wrong!");
                return "/myaccount";
            }

            session.setAttribute("user", user);
        }

        model.addAttribute("user", user);
        return "myaccount";
    }

    /**
     * Renders account edit page where a user can edit their data.
     * @param model model to pass error messages or user data to account
     *              edit page.
     * @param request Used for session data to get the authenticated
     *                user's details.
     * @return myaccount-edit (account edit page)
     */
    @GetMapping("/my-account/edit")
    public String editMyAccount(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        NewUser editUser = new NewUser();
        model.addAttribute("user", user);
        model.addAttribute("editUser", editUser);
        return "myaccount-edit";
    }

    /**
     * Handles account edit form submission when a user saves new account data.
     * @param model model to pass error messages or user data to account
     *              edit page.
     * @param request Used for session data to get the authenticated
     *                user's details.
     * @param editedUserDetails Form data from form submission with new user data.
     * @return myaccount-edit (account edit page if error)
     * @return redirect:/my-account (redirect to my-account if successful)
     */
    @PostMapping("/my-account")
    public String handleSubmitEditMyAccount(Model model, HttpServletRequest request, User editedUserDetails) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        editedUserDetails.setId(user.getId());

        try {
            UserDataFetcher.update(editedUserDetails);
        } catch (Exception error) {
            model.addAttribute("error", "There was an error processing your request.");
            return "myaccount-edit";
        }

        user.setName(editedUserDetails.getName());
        user.setEmail(editedUserDetails.getEmail());
        user.setNickname(editedUserDetails.getNickname());
        session.setAttribute("user", user);

        model.addAttribute("user", user);
        return "redirect:my-account";
    }

    /**
     * Handles when a user deletes their account.
     * @param request Used for session data to get the authenticated
     *                user's details.
     * @param authentication Used to set authentication to false if successful.
     * @param id The user's id to be deleted.
     * @return redirect:/myaccount (account page if error)
     * @return redirect:/ (redirect home if successful)
     */
    @GetMapping("/delete")
    public String deleteUser(@RequestParam Long id, Authentication authentication, HttpServletRequest request) {
        HttpSession session = request.getSession();

        try {
            UserDataFetcher.delete(id);
        } catch (Exception error) {
            return "redirect:my-account";
        }

        authentication.setAuthenticated(false);
        session.invalidate();

        return "redirect:/";
    }
}

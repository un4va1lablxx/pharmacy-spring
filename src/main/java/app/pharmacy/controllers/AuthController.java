package app.pharmacy.controllers;

import app.pharmacy.entities.User;
import app.pharmacy.services.UserService;
import app.pharmacy.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final CartService cartService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("error", "Неверный email или пароль");
        }
        if (logout != null) {
            model.addAttribute("message", "Вы успешно вышли из системы");
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        RedirectAttributes redirectAttributes) {
        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam("username") String userName,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam("firstName") String firstName,
                           @RequestParam("lastName") String lastName,
                           @RequestParam(value = "middleName", required = false) String middleName,
                           @RequestParam(value = "phone", required = false) String phone,
                           RedirectAttributes redirectAttributes) {

        try {
            System.out.println("Received registration data:");
            System.out.println("Username: " + userName);
            System.out.println("Email: " + email);
            System.out.println("First Name: " + firstName);
            System.out.println("Last Name: " + lastName);
            System.out.println("Middle Name: " + middleName);
            System.out.println("Phone: " + phone);

            if (userService.existsByEmail(email)) {
                redirectAttributes.addFlashAttribute("error", "Пользователь с таким email уже существует");
                return "redirect:/register";
            }

            if (userService.existsByUsername(userName)) {
                redirectAttributes.addFlashAttribute("error", "Пользователь с таким логином уже существует");
                return "redirect:/register";
            }

            User user = new User();
            user.setId(UUID.randomUUID());
            user.setUserName(userName);
            user.setEmail(email);
            user.setPasswordHash(passwordEncoder.encode(password));
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setMiddleName(middleName);
            user.setPhone(phone);

            User savedUser = userService.save(user);
            cartService.saveCartToDatabase(savedUser);

            redirectAttributes.addFlashAttribute("success", "Регистрация прошла успешно! Теперь вы можете войти в систему.");
            return "redirect:/login";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Ошибка при регистрации: " + e.getMessage());
            return "redirect:/register";
        }
    }

    @PostMapping("/logout-with-save")
    public String logoutWithSave(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "Вы успешно вышли из системы");
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String simpleLogout(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "Вы успешно вышли из системы");
        return "redirect:/login";
    }
}
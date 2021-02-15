package com.aleksandrchuyko.springbooterbroad.controllers;


import com.aleksandrchuyko.springbooterbroad.domain.User;
import com.aleksandrchuyko.springbooterbroad.service.Session;
import com.aleksandrchuyko.springbooterbroad.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

@Controller
public class UsersTable {

    @Autowired
    private  UserService userService;

    @Autowired
    private  Session session;



    @GetMapping("/admin")
    public String userList(Model model, HttpSession session) {
        User user = (User) userService.loadUserByUsername(this.session.getUsername(session.getId()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        user.setLastLoginDate(simpleDateFormat.format(new Date()));
        userService.update(user);
        model.addAttribute("allUsers", userService.allUsers());
        model.addAttribute("usr", user.getUsername());
        return "userTableView";
    }

    @PostMapping("/admin")
    public String processUsers(@RequestParam Map<String, String> form,
                               HttpSession session, Model model) {
        System.out.println(form);
        Iterator<Map.Entry<String, String>> entries = form.entrySet().iterator();
        entries.next();
        boolean bool = false;
        switch (entries.next().getKey()) {
            case "block":
                bool = block(entries, session.getId());
                break;
            case "delete":
                bool = delete(entries, session.getId());
                break;
            case "unblock":
                unblock(entries);
        }
        if (bool) {
            return "redirect:/login";
        }
        model.addAttribute("allUsers", userService.allUsers());
        model.addAttribute("usr",
                userService.loadUserByUsername(this.session.getUsername(session.getId())).getUsername());
        return "userTableView";
    }

    private boolean block(Iterator<Map.Entry<String, String>> entries, String sessionID) {
        boolean bool = false;
        while (entries.hasNext()) {
            User user = userService.findUserById(Long.parseLong(entries.next().getKey()));
            user.setStatus("Blocked");
            userService.update(user);
            if (session.expireUserSessions(user.getUsername(), sessionID)) {
                bool = true;
            }
        }
        return bool;
    }

    private void unblock(Iterator<Map.Entry<String, String>> entries) {
        while (entries.hasNext()) {
            User user = userService.findUserById(Long.parseLong(entries.next().getKey()));
            user.setStatus("Active");
            userService.update(user);
        }
    }

    private boolean delete(Iterator<Map.Entry<String, String>> entries, String sessionID) {
        boolean bool = false;
        while (entries.hasNext()) {
            User user = userService.findUserById(Long.parseLong(entries.next().getKey()));
            userService.deleteUser(user.getId());
            if (session.expireUserSessions(user.getUsername(), sessionID)) {
                bool = true;
            }
        }
        return bool;
    }
}

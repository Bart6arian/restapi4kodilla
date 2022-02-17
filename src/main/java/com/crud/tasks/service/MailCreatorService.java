package com.crud.tasks.service;

import com.crud.tasks.config.AdminConfig;
import com.crud.tasks.config.CompanyConfiguration;
import com.crud.tasks.domain.TrelloBoardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;

@Service
public class MailCreatorService {

    @Autowired
    TrelloService service;

    @Autowired
    private AdminConfig adminConfig;

    @Autowired
    private CompanyConfiguration companyConfiguration;

    @Autowired
    @Qualifier("templateEngine")
    private TemplateEngine templateEngine;

    public String buildTrelloCardEmail(String message) {

        List<String> functionality = new ArrayList<>();
        functionality.add("You can manage your tasks");
        functionality.add("Provides connection with Trello Account");
        functionality.add("Application allows sending tasks to Trello");

        Context context = new Context();
        context.setVariable("message", message);
        context.setVariable("tasks_url", "http://localhost:8080/v1/createTask");
        context.setVariable("button", "Visit website");
        context.setVariable("admin_name", adminConfig.getAdminName());
        context.setVariable("company_name", companyConfiguration.getCompanyName());
        context.setVariable("company_goal", companyConfiguration.getCompanyGoal());
        context.setVariable("company_email", companyConfiguration.getCompanyMail());
        context.setVariable("company_phone", companyConfiguration.getCompanyPhone());
        context.setVariable("show_button", false);
        context.setVariable("is_friend", true);
        context.setVariable("admin_config", adminConfig);
        context.setVariable("application_functionality", functionality);
        return templateEngine.process("mail/created-trello-card-mail", context);
    }

    public String showTasksTodo(String message) {

        List<TrelloBoardDto> tasks = service.fetchTrelloBoards();

        Context context = new Context();
        context.setVariable("message", message);
        context.setVariable("tasks_url", "http://localhost:8080/v1/getTasks");
        context.setVariable("button", "View task");
        context.setVariable("show_button", true);
        context.setVariable("is_friend", true);
        context.setVariable("tasks", tasks);

        return templateEngine.process("mail/tasks-todo", context);
    }
}

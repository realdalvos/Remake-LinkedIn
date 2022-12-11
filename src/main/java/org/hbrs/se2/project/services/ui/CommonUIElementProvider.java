package org.hbrs.se2.project.services.ui;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import org.hbrs.se2.project.control.InboxControl;
import org.hbrs.se2.project.control.UserControl;
import org.hbrs.se2.project.dtos.ConversationDTO;
import org.hbrs.se2.project.dtos.MessageDTO;
import org.hbrs.se2.project.dtos.impl.ConversationDTOImpl;
import org.hbrs.se2.project.dtos.impl.MessageDTOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class CommonUIElementProvider {

    @Autowired
    UserControl userControl;
    @Autowired
    InboxControl inboxControl;

    /**
     * Creates an Error Dialog
     *
     * @param message Takes the message String as an input
     */
    public void makeDialog(String message) {
        VerticalLayout layout = new VerticalLayout();
        Dialog dialog = new Dialog();
        Button close = new Button("OK");
        close.addClickListener(event -> dialog.close());
        layout.add(new Text(message), close);
        layout.setHorizontalComponentAlignment(FlexComponent.Alignment.END, close);
        dialog.add(layout);
        dialog.open();
    }

    public void makeYesNoDialog(String message, ComponentEventListener<ClickEvent<Button>> listener) {
        VerticalLayout vLayout = new VerticalLayout();
        Dialog dialog = new Dialog();
        Button no = new Button("Nein");
        no.addClickListener(event -> dialog.close());
        Button yes = new Button("Ja");
        yes.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        yes.addClickListener(listener);
        yes.addClickListener(event -> UI.getCurrent().getPage().reload());
        yes.addClickListener(event -> dialog.close());
        HorizontalLayout hLayout = new HorizontalLayout();
        vLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.END, hLayout);
        hLayout.add(no, yes);
        vLayout.add(new Text(message), hLayout);
        dialog.add(vLayout);
        dialog.open();
    }

    public void makeConfirm(String message, ComponentEventListener<ClickEvent<Button>> listener) {
        VerticalLayout vLayout = new VerticalLayout();
        Dialog dialog = new Dialog();
        Button close = new Button("Abbrechen");
        close.addClickListener(event -> dialog.close());
        Button reject = new Button("Verwerfen");
        reject.addClickListener(event -> UI.getCurrent().getPage().reload());
        Button save = new Button("Speichern");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(listener);
        save.addClickListener(event -> dialog.close());
        HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.add(close, reject, save);
        vLayout.add(new Text(message), hLayout);
        vLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, hLayout);
        dialog.add(vLayout);
        dialog.open();
    }

    public void conversationDialog(int companyid, int studentid, int jobid) {
        Dialog dialog = new Dialog();
        VerticalLayout vLayout = new VerticalLayout();
        TextField title = new TextField("Betreff");
        TextArea content = new TextArea("Nachricht");
        HorizontalLayout buttons = new HorizontalLayout();
        Button close = new Button("Abbrechen");
        close.addClickListener(event -> dialog.close());
        Button send = new Button("Senden");
        send.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        send.addClickListener(event -> {
            ConversationDTO newConversation = new ConversationDTOImpl();
            newConversation.setCompanyid(companyid);
            newConversation.setStudentid(studentid);
            newConversation.setJobid(jobid);
            newConversation.setTitle(title.getValue());
            ConversationDTO conversation = inboxControl.newConversation(newConversation);
            MessageDTO message = new MessageDTOImpl();
            message.setConversationid(conversation.getConversationid());
            message.setContent(content.getValue());
            message.setTimestamp(Instant.now());
            message.setUserid(userControl.getCurrentUser().getUserid());
            inboxControl.saveMessage(message);
            dialog.close();
        });
        buttons.add(close, send);
        vLayout.add(new Text("Kontakt:"), title, content, buttons);
        vLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, buttons);
        dialog.add(vLayout);
        dialog.open();
    }

    public VerticalLayout introductionText(String headline, String description) {
        VerticalLayout vLayout = new VerticalLayout();
        H2 startText = new H2(headline);

        H3 descriptionText = new H3(description);

        vLayout.setSizeFull();
        vLayout.setPadding(false);
        vLayout.setSpacing(true);
        vLayout.getThemeList().set("spacing-s", true);
        vLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        startText.getElement().getStyle().set("font-size","45px"); // font size
        startText.getElement().getStyle().set("color", "#f2a6b4"); // hex value of color in custom Theme for continuity
        startText.getElement().getStyle().set("text-align","center"); // text is now in center

        descriptionText.getElement().getStyle().set("font-size","20px");
        descriptionText.getElement().getStyle().set("text-align","center");

        vLayout.add(startText);
        vLayout.add(descriptionText);
        vLayout.add(new Label());

        return vLayout;
    }

}

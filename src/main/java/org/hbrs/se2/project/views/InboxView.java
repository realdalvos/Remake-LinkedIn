package org.hbrs.se2.project.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import org.hbrs.se2.project.control.InboxControl;
import org.hbrs.se2.project.control.UserControl;
import org.hbrs.se2.project.dtos.ConversationDTO;
import org.hbrs.se2.project.dtos.MessageDTO;
import org.hbrs.se2.project.dtos.impl.ConversationDTOImpl;
import org.hbrs.se2.project.dtos.impl.MessageDTOImpl;
import org.hbrs.se2.project.services.ui.CommonUIElementProvider;
import org.hbrs.se2.project.util.Globals;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@JsModule("@vaadin/vaadin-lumo-styles/badge.js")
@CssImport(value = "./themes/mytheme/styles.css", include = "lumo-badge")
public abstract class InboxView extends Div {

    @Autowired
    private CommonUIElementProvider ui;

    protected InboxControl inboxControl;
    protected UserControl userControl;

    protected Grid<ConversationDTO> conversationGrid = new Grid<>();
    protected Grid<ConversationDTO> chatHeader = new Grid<>();
    protected Grid<ConversationDTO> chat = new Grid<>();
    protected VerticalLayout chatLayout = new VerticalLayout(chatHeader, chat);
    protected SplitLayout mainLayout = new SplitLayout(conversationGrid, chatLayout);

    protected Grid<ConversationDTO> conversationLayout(MessageList list, ConversationDTO conversation) {
        Grid<ConversationDTO> grid = new Grid<>();
        grid.setItems(conversation);
        grid.setHeight("90%");
        grid.setWidthFull();
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.addComponentColumn(component -> {
            list.setWidth("70%");
            List<MessageListItem> items = new ArrayList<>(list.getItems());
            TextArea content = new TextArea();
            content.setWidth("70%");
            content.setHelperText("Nachricht");
            Button send = new Button("Senden");
            HorizontalLayout input = new HorizontalLayout(content, send);
            input.setWidth("80%");
            VerticalLayout layout = new VerticalLayout(list, input);
            layout.setWidthFull();
            if (conversation.getCompanyid() != null && conversation.getStudentid() != null) {
                send.addClickListener(event -> {
                    if (!content.isEmpty()) {
                        MessageListItem newMessage = new MessageListItem(
                                content.getValue(), Instant.now(), userControl.getCurrentUser().getUsername());
                        items.add(newMessage);
                        list.setItems(items);
                        MessageDTO message = new MessageDTOImpl();
                        message.setConversationid(conversation.getConversationid());
                        message.setContent(newMessage.getText());
                        message.setTimestamp(newMessage.getTime());
                        message.setUserid(userControl.getCurrentUser().getUserid());
                        inboxControl.saveMessage(message);
                        content.clear();
                    }
                });
            } else {
                content.setEnabled(false);
                content.setValue("Gesprächsteilnehmer nicht verfügbar");
                send.setEnabled(false);
            }
            layout.setAlignItems(FlexComponent.Alignment.CENTER);
            layout.setSizeFull();
            return  layout;
        });
        return grid;
    }

    protected Grid<ConversationDTO> conversationHeader(ConversationDTO conversation) {
        H3 title = new H3(conversation.getTitle());
        Button delete = new Button(new Icon(VaadinIcon.TRASH));
        delete.addClickListener(click -> {
            ui.makeYesNoDialog("Möchtest du die Konversation beenden und aus deiner Übersicht entfernen?", event -> {
                if(Objects.equals(userControl.getCurrentUser().getRole(), Globals.Roles.STUDENT)) {
                    inboxControl.endConversationStudent(conversation);
                } else if(Objects.equals(userControl.getCurrentUser().getRole(), Globals.Roles.COMPANY)) {
                    inboxControl.endConversationCompany(conversation);
                }
                UI.getCurrent().getPage().reload();
            });
        });
        Grid<ConversationDTO> grid = new Grid<>();
        grid.setItems(conversation);
        grid.setHeight("10%");
        grid.setWidthFull();
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.addComponentColumn(component -> title).setTextAlign(ColumnTextAlign.END);
        grid.addComponentColumn(component -> delete).setWidth("10%").setTextAlign(ColumnTextAlign.END);
        return grid;
    }

    protected Grid<ConversationDTO> conversationGrid(Set<ConversationDTO> conversations, int unread) {
        conversationGrid.setItems(conversations);
        conversationGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        conversationGrid.setHeightFull();
        mainLayout.setSplitterPosition(40);
        mainLayout.setHeight("100%");
        chatHeader.setItems(new ConversationDTOImpl());
        chatHeader.setHeight("10%");
        chatHeader.setSelectionMode(Grid.SelectionMode.NONE);
        chat.setHeight("90%");
        chat.setSelectionMode(Grid.SelectionMode.NONE);
        chatHeader.addComponentColumn(component -> new H3("Du hast " + unread + " ungelesene " + (unread == 1 ? "Nachricht!" : "Nachrichten!")))
                .setTextAlign(ColumnTextAlign.CENTER);
        return conversationGrid;
    }

    protected VerticalLayout conversationComponent(ConversationDTO conversation, String name) {
        VerticalLayout component = new VerticalLayout();
        HorizontalLayout header = new HorizontalLayout(new Text(name));
        Span title = new Span(conversation.getTitle());
        title.setWidthFull();
        Text jobTitle = new Text(inboxControl.getJob(conversation.getJobid()));
        component.add(header, title, jobTitle);
        return component;
    }

    protected Span unreadMessages(ConversationDTO conversation) {
        int unread = inboxControl.getNumberOfUnreadMessagesFromConversation(conversation.getConversationid(), userControl.getCurrentUser().getUserid());
        Icon icon = new Icon(VaadinIcon.ENVELOPE);
        icon.getStyle().set("padding", "var(--lumo-space-xs");
        Span info = new Span(Integer.toString(unread));
        Span badge = new Span(info, icon);
        badge.getElement().getThemeList().add("badge success");
        if (unread == 0) {
            badge.setVisible(false);
        }
        return badge;
    }

    protected Span latestMessage(ConversationDTO conversation) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy").withZone(ZoneId.systemDefault());
        return new Span(formatter.format(inboxControl.getLatestMessageTime(conversation.getConversationid())));
    }

}

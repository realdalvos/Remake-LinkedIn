package org.hbrs.se2.project.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
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
import com.vaadin.flow.component.textfield.TextArea;
import org.hbrs.se2.project.control.InboxControl;
import org.hbrs.se2.project.control.UserControl;
import org.hbrs.se2.project.dtos.ConversationDTO;
import org.hbrs.se2.project.dtos.MessageDTO;
import org.hbrs.se2.project.dtos.impl.MessageDTOImpl;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@JsModule("@vaadin/vaadin-lumo-styles/badge.js")
@CssImport(value = "./themes/mytheme/styles.css", include = "lumo-badge")
public abstract class InboxView extends Div {

    protected InboxControl inboxControl;
    protected UserControl userControl;

    protected Grid<ConversationDTO> grid = new Grid<>();
    protected HorizontalLayout layout = new HorizontalLayout(grid, new VerticalLayout());

    protected VerticalLayout conversationLayout(MessageList list, ConversationDTO conversation) {
        H3 title = new H3(conversation.getTitle());
        list.setWidth("70%");
        List<MessageListItem> items = new ArrayList<>(list.getItems());
        TextArea content = new TextArea();
        content.setWidth("70%");
        content.setHelperText("Nachricht");
        Button send = new Button("Senden");
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
            }
        });
        HorizontalLayout input = new HorizontalLayout(content, send);
        input.setWidth("80%");
        VerticalLayout layout = new VerticalLayout(title, list, input);
        layout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, title, list, input);
        return layout;
    }

    protected Grid<ConversationDTO> conversationGrid(List<ConversationDTO> conversations) {
        grid.setItems(conversations);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setHeightByRows(true);
        return grid;
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
        int unread = inboxControl.getNumberOfUnreadMessages(conversation.getConversationid(), userControl.getCurrentUser().getUserid());
        Icon icon = new Icon(VaadinIcon.INFO_CIRCLE);
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

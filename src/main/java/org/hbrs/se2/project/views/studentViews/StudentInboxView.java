package org.hbrs.se2.project.views.studentViews;

import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.hbrs.se2.project.control.InboxControl;
import org.hbrs.se2.project.control.UserControl;
import org.hbrs.se2.project.util.Globals;
import org.hbrs.se2.project.views.AppView;
import org.hbrs.se2.project.views.InboxView;

@Route(value = Globals.Pages.STUDENT_INBOX_VIEW, layout = AppView.class, registerAtStartup = false)
@PageTitle("Postfach")
public class StudentInboxView extends InboxView {

    public StudentInboxView(InboxControl inboxControl, UserControl userControl) {
        this.inboxControl = inboxControl;
        this.userControl = userControl;

        conversationGrid = conversationGrid(inboxControl.getConversationsOfStudent(userControl.getStudentProfile(userControl.getCurrentUser().getUserid()).getStudentid()),
                inboxControl.getNumberOfUnreadMessagesFromStudent(userControl.getCurrentUser().getUserid()));
        conversationGrid.addComponentColumn(conversation -> conversationComponent(conversation,
                inboxControl.getNameOfCompanyFromConversation(conversation))).setWidth("70%").setHeader("Posteingang");
        conversationGrid.addComponentColumn(this::unreadMessages).setWidth("10%").setTextAlign(ColumnTextAlign.END);
        conversationGrid.addComponentColumn(this::latestMessage).setWidth("20%").setTextAlign(ColumnTextAlign.END);
        conversationGrid.addSelectionListener(select -> select.getFirstSelectedItem().ifPresent(conversation -> {
            mainRight.replace(mainRight.getComponentAt(0), conversationHeader(conversation));
            mainRight.replace(mainRight.getComponentAt(1), conversationLayout(inboxControl.getMessagesOfStudent(conversation), conversation));
        }));
        setSizeFull();
        setHeightFull();
        add(mainLayout);
    }

}

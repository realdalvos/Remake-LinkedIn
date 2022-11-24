package org.hbrs.se2.project.services.ui;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.stereotype.Service;

@Service
public class CommonUIElementProvider {

    /**
     * Creates an Error Dialog
     *
     * @param message Takes the message String as an input
     */
    public void makeDialog(String message) {
        VerticalLayout layout = new VerticalLayout();
        Dialog dialog = new Dialog();
        // close button
        Button closeb = new Button("OK");
        closeb.addClickListener(event -> dialog.close());
        layout.add(new Text(message), closeb);
        layout.setHorizontalComponentAlignment(FlexComponent.Alignment.END, closeb);
        dialog.add(layout);
        dialog.setWidth("400px");
        dialog.setHeight("150px");
        dialog.open();
    }

    public void makeYesNoDialog(String message, Button yes) {
        VerticalLayout vLayout = new VerticalLayout();
        Dialog dialog = new Dialog();
        Button no = new Button("Nein");
        no.addClickListener(event -> dialog.close());
        yes.setText("Ja");
        yes.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        yes.addClickListener(event -> dialog.close());
        HorizontalLayout hLayout = new HorizontalLayout();
        vLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.END, hLayout);
        hLayout.add(no, yes);
        vLayout.add(new Text(message), hLayout);
        dialog.add(vLayout);
        dialog.open();
    }

    public void makeConfirm(String message, Button save) {
        Dialog dialog = new Dialog();
        dialog.setWidth("500px");
        dialog.setHeight("200px");
        Button close = new Button("Abbrechen");
        close.addClickListener(event -> dialog.close());
        Button reject = new Button("Verwerfen");
        reject.addClickListener(event -> UI.getCurrent().getPage().reload());
        save.setText("Speichern");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(event -> dialog.close());
        dialog.add(new VerticalLayout(new Text(message), new HorizontalLayout(close, reject, save)));
        dialog.open();
    }

}

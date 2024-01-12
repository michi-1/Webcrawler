package org.example.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EmailMessageBuilder {

    public static String buildEmailMessage() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String formattedDate = dateFormat.format(now);


        StringBuilder message = new StringBuilder();
        message.append("Sehr geehrter Kunde,<br><br>");
        message.append("Wir freuen uns, Ihnen unseren neuesten Fahrzeugbericht präsentieren zu können. ");
        message.append("In diesem Bericht finden Sie eine Übersicht über die neuesten Fahrzeuge auf dem Markt, ");
        message.append("die Ihren spezifischen Kriterien entsprechen.<br><br>");
        message.append("Unsere Experten haben sorgfältig die Daten von verschiedenen Quellen gesammelt und analysiert, ");
        message.append("um Ihnen eine Auswahl an hochwertigen Fahrzeugen vorzustellen. ");
        message.append("Wir verstehen, wie wichtig es ist, das perfekte Auto zu finden, ");
        message.append("das Ihren Bedürfnissen und Wünschen entspricht.<br><br>");
        message.append("In diesem Bericht werden Sie Informationen zu den neuesten Automodellen, Marken, Preisen und Jahresmodellen finden. ");
        message.append("Wir haben sicherzustellen versucht, dass alle aufgeführten Fahrzeuge Ihren Anforderungen entsprechen und eine Vielzahl von Optionen bieten.<br><br>");
        message.append("Es war uns eine Freude, diesen Bericht für Sie zu erstellen, ");
        message.append("und wir hoffen, dass er Ihnen bei Ihrer Suche nach einem neuen Fahrzeug hilfreich ist. ");
        message.append("Bei Fragen oder weiterem Bedarf stehen wir Ihnen gerne zur Verfügung.<br><br>");
        message.append("Vielen Dank, dass Sie unsere Dienstleistungen in Anspruch nehmen. ");
        message.append("Wir wünschen Ihnen viel Erfolg bei der Suche nach Ihrem Traumauto!<br><br>");
        message.append("Mit freundlichen Grüßen,<br><br>");
        message.append("Ihr Carcrawlpro Team<br>");
        message.append("Datum und Uhrzeit: ").append(formattedDate);
        message.append("<br><br>");


        return message.toString();
    }
}

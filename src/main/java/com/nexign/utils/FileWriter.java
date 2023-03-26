package com.nexign.utils;

import com.nexign.model.SubscriberInfo;

import java.io.IOException;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.io.File.separator;
import static java.lang.String.format;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Paths.get;
import static java.text.DecimalFormatSymbols.getInstance;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Locale.US;
import static java.util.stream.Collectors.joining;

public class FileWriter {
    // Имена заголовков таблицы
    private static final String TARIFF_INDEX = "Tariff index: %s%n";
    private static final String PHONE_NUMBER = "Report for phone number %s:%n";
    private static final String CALL_TYPE = "Call Type";
    private static final String START_TIME = "Start Time";
    private static final String END_TIME = "End Time";
    private static final String DURATION = "Duration";
    private static final String COST = "Cost";
    private static final String TOTAL_COST = "Total Cost:";
    private static final String RUBLES = "rubles";

    // Разделители таблицы
    private static final String SPACE = " ";
    private static final String LINE_SEPARATOR = "-";
    private static final String SEPARATOR = "|";
    private static final int MAX_SIZE_COLUMN = 79;

    private static final DateTimeFormatter OUTPUT_FORMATTER;
    private static final DecimalFormat DF;
    private static final String TABLE_TITLE_FORMAT;
    private static final String TABLE_BODY_FORMAT;
    private static final String TABLE_TOTAL_FORMAT;

    static {
        OUTPUT_FORMATTER = ofPattern("yyyy-MM-dd HH:mm:ss");
        DF = new DecimalFormat("0.00", getInstance(US));
        TABLE_TITLE_FORMAT = format("%s%s%s%2$s%1$s%2$3s%s%2$8s%1$s%2$5s%s%2$8s%1$s%2$s%s%2$s%1$s%2$s%s%2$5s%1$s%n",
                SEPARATOR, SPACE, CALL_TYPE, START_TIME, END_TIME, DURATION, COST);
        TABLE_BODY_FORMAT = format("%s%5s%%s%2$4s%1$s%2$s%%19s%2$s%1$s%2$s%%19s%2$s%1$s%2$s%%-8s%2$s%1$s%2$s%%8s%2$s%1$s%n",
                SEPARATOR, SPACE);
        TABLE_TOTAL_FORMAT = format("%s%43s%s%2$s%1$s%2$s%%12s%2$s%s%2$s%1$s%n",
                SEPARATOR, SPACE, TOTAL_COST, RUBLES);
    }

    public void writeToFile(Map<String, Set<SubscriberInfo>> map) throws IOException {
        Path reportsDirectory = createDirectories(get("reports"));

        for (Map.Entry<String, Set<SubscriberInfo>> entry : map.entrySet()) {
            try (java.io.FileWriter writer =
                         new java.io.FileWriter(reportsDirectory + separator + "report_" + entry.getKey())) {

                writer.write(format(TARIFF_INDEX,
                        entry.getValue().stream().map(c -> c.getTariffType().getTariff()).distinct().collect(joining())));
                writer.write(LINE_SEPARATOR.repeat(MAX_SIZE_COLUMN) + "\n");
                writer.write(format(PHONE_NUMBER, entry.getKey()));
                writer.write(LINE_SEPARATOR.repeat(MAX_SIZE_COLUMN) + "\n");
                writer.write(TABLE_TITLE_FORMAT);
                writer.write(LINE_SEPARATOR.repeat(MAX_SIZE_COLUMN) + "\n");

                for (SubscriberInfo subscriber : entry.getValue()) {
                    writer.write(format(TABLE_BODY_FORMAT,
                            subscriber.getCallType().getIndex(),
                            subscriber.getCallStart().format(OUTPUT_FORMATTER),
                            subscriber.getCallEnd().format(OUTPUT_FORMATTER),
                            subscriber.getDuration().toString(),
                            DF.format(subscriber.getCost())));
                }

                writer.write(LINE_SEPARATOR.repeat(MAX_SIZE_COLUMN) + "\n");
                writer.write(format(TABLE_TOTAL_FORMAT, DF.format(getCost(entry.getValue().stream()))));
                writer.write(LINE_SEPARATOR.repeat(MAX_SIZE_COLUMN) + "\n");
            }
        }
    }

    private double getCost(Stream<SubscriberInfo> stream) {
        return stream.map(SubscriberInfo::getCost).reduce(0.0, Double::sum);
    }
}

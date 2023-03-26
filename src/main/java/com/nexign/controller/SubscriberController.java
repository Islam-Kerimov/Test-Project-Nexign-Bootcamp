package com.nexign.controller;

import com.nexign.model.CallType;
import com.nexign.model.SubscriberInfo;
import com.nexign.model.TariffType;
import com.nexign.model.tariff.ByMinuteTariff;
import com.nexign.model.tariff.OrdinaryTariff;
import com.nexign.model.tariff.Tariff;
import com.nexign.model.tariff.UnlimitedTariff;
import com.nexign.utils.FileWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.time.LocalTime.ofSecondOfDay;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Comparator.comparing;

public class SubscriberController {
    private static final DateTimeFormatter INPUT_FORMATTER = ofPattern("yyyyMMddHHmmss");
    private static final DateTimeFormatter OUTPUT_FORMATTER = ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String CDR_FILE = "cdr.txt";
    private static final FileWriter FILE_WRITER = new FileWriter();

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CDR_FILE))) {
            Map<String, Set<SubscriberInfo>> allSubscribers = readAllFile(reader);
            calculateCost(allSubscribers);
            FILE_WRITER.writeToFile(allSubscribers);
        } catch (IOException ioe) {
            throw new RuntimeException("Ошибка во время чтения/записи файла: " + ioe);
        }
    }

    private static void calculateCost(Map<String, Set<SubscriberInfo>> map) {
        for (Map.Entry<String, Set<SubscriberInfo>> entry : map.entrySet()) {
            Optional<TariffType> tariffType = entry.getValue().stream()
                    .map(SubscriberInfo::getTariffType)
                    .findAny();

            if (tariffType.isPresent()) {
                Tariff tariff = null;
                switch (TariffType.valueOf(tariffType.get().name())) {
                    case UNLIMITED:
                        tariff = new UnlimitedTariff();
                        break;
                    case BY_MINUTE:
                        tariff = new ByMinuteTariff();
                        break;
                    case ORDINARY:
                        tariff = new OrdinaryTariff();
                }

                costCall(entry.getValue(), tariff);
            }
        }
    }

    private static void costCall(Set<SubscriberInfo> value, Tariff tariff) {
        for (SubscriberInfo subscriber : value) {
            double cost = tariff.addCostCall(subscriber.getCallType(), subscriber.getDuration());
            subscriber.setCost(cost);
        }
    }

    private static Map<String, Set<SubscriberInfo>> readAllFile(BufferedReader reader) throws IOException {
        Map<String, Set<SubscriberInfo>> map = new HashMap<>();
        while (true) {
            String line = reader.readLine();
            if (line == null || line.isEmpty()) {
                break;
            }

            String[] data = line
                    .replaceAll(",", "")
                    .split(" ");
            SubscriberInfo subscriberInfo = getSubscriber(data);

            Set<SubscriberInfo> infos;
            if (map.containsKey(data[1])) {
                infos = map.get(data[1]);
            } else {
                infos = new TreeSet<>(comparing(SubscriberInfo::getCallStart));
            }
            infos.add(subscriberInfo);
            map.put(data[1], infos);
        }
        return map;
    }

    private static SubscriberInfo getSubscriber(String[] info) {
        SubscriberInfo subscriberInfo = new SubscriberInfo();
        subscriberInfo.setCallType(CallType.fromString(info[0]));
        subscriberInfo.setPhoneNumber(info[1]);
        subscriberInfo.setCallStart(getDateTime(info[2]));
        subscriberInfo.setCallEnd(getDateTime(info[3]));
        subscriberInfo.setDuration(getDurationTime(subscriberInfo.getCallStart(), subscriberInfo.getCallEnd()));
        subscriberInfo.setTariffType(TariffType.fromString(info[4]));
        return subscriberInfo;
    }

    private static LocalDateTime getDateTime(String str) {
        String inputFormat = LocalDateTime.parse(str, INPUT_FORMATTER).format(OUTPUT_FORMATTER);
        return LocalDateTime.parse(inputFormat, OUTPUT_FORMATTER);
    }

    private static LocalTime getDurationTime(LocalDateTime start, LocalDateTime end) {
        long timeSeconds = start.until(end, SECONDS);
        return ofSecondOfDay(timeSeconds);
    }
}
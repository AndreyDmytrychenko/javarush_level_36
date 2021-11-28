package com.javarush.task39.task3913;

import com.javarush.task39.task3913.query.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LogParser implements IPQuery, UserQuery, DateQuery, EventQuery, QLQuery {

    List<LogEntry> logList = new ArrayList<>();

    public LogParser(Path logDir) {

        listFilesForFolder(logDir);

    }

    public void listFilesForFolder(Path logDir){
        File[] listOfFiles = logDir.toFile().listFiles((dir, name) -> name.endsWith(".log"));

        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                logList.addAll(getList(file));
            }
        }
    }

    public Date dateParser(String date) {
        Date result = null;
        SimpleDateFormat format = new SimpleDateFormat("d.M.yyyy H:m:s");

        try {
            result = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<LogEntry> getList(File file) {
        String line;
        List<LogEntry> list = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.ready()) {
                line = reader.readLine();
                if (line.isEmpty()) continue;
                String[] info = line.split(" {2}");
                LogEntry entry = new LogEntry();
                entry.setIp(info[0]);
                entry.setUser(info[1]);
                entry.setDate(dateParser(info[2]));
                for (Event event : Event.values()) {
                    String[] eventData = info[3].split(" ");
                    if (eventData.length == 2)
                        entry.setEventNumber(Integer.parseInt(eventData[1]));
                    if (event.toString().equals(eventData[0]))
                        entry.setEvent(event);
                }
                for(Status status : Status.values()) {
                    if (status.toString().equals(info[4]))
                        entry.setStatus(status);
                }
                list.add(entry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean dateBetweenDates(Date current, Date after, Date before) {
        if (after == null) {
            after = new Date(0);
        }
        if (before == null) {
            before = new Date(Long.MAX_VALUE);
        }
        return current.after(after) && current.before(before);
    }

    private Event readEvent(String lineToParse) {
        Event event = null;
        if (lineToParse.contains("SOLVE_TASK")) {
            event = Event.SOLVE_TASK;
        } else if (lineToParse.contains("DONE_TASK")) {
            event = Event.DONE_TASK;
        } else {
            switch (lineToParse) {
                case "LOGIN": {
                    event = Event.LOGIN;
                    break;
                }
                case "DOWNLOAD_PLUGIN": {
                    event = Event.DOWNLOAD_PLUGIN;
                    break;
                }
                case "WRITE_MESSAGE": {
                    event = Event.WRITE_MESSAGE;
                    break;
                }
            }
        }
        return event;
    }

    private Status readStatus(String lineToParse) {
        Status status = null;
        switch (lineToParse) {
            case "OK": {
                status = Status.OK;
                break;
            }
            case "FAILED": {
                status = Status.FAILED;
                break;
            }
            case "ERROR": {
                status = Status.ERROR;
                break;
            }
        }
        return status;
    }



    private Object getCurrentValue(LogEntry logEntry, String field) {
        Object value = null;
        switch (field) {
            case "ip": {
                Command method = new GetIpCommand(logEntry);
                value = method.execute();
                break;
            }
            case "user": {
                Command method = new GetUserCommand(logEntry);
                value = method.execute();
                break;
            }
            case "date": {
                Command method = new GetDateCommand(logEntry);
                value = method.execute();
                break;
            }
            case "event": {
                Command method = new GetEventCommand(logEntry);
                value = method.execute();
                break;
            }
            case "status": {
                Command method = new GetStatusCommand(logEntry);
                value = method.execute();
                break;
            }
        }
        return value;
    }

    private Boolean equalsValues(LogEntry entry, String field, String value2) {

        if (field == null)
            field = "";
        if (value2 == null)
            value2 = "";

        Object value1 = getCurrentValue(entry, field);

        if (value1 instanceof String) return value1.equals(value2);
        else if (value1 instanceof Date) return value1.equals(dateParser(value2));
        else if (value1 instanceof Event) return value1 == readEvent(value2);
        else if (value1 instanceof Status) return value1 == readStatus(value2);
        else return true;
    }




    @Override
    public int getNumberOfUniqueIPs(Date after, Date before) {

        return getUniqueIPs(after, before).size();
    }

    @Override
    public Set<String> getUniqueIPs(Date after, Date before) {

            return logList
                    .stream()
                    .filter(entry -> dateBetweenDates(entry.getDate(), after, before))
                    .map(LogEntry::getIp)
                    .collect(Collectors.toSet());
    }

    @Override
    public Set<String> getIPsForUser(String user, Date after, Date before) {

            return logList
                    .stream()
                    .filter(entry -> dateBetweenDates(entry.getDate(), after, before) && entry.getUser().equals(user))
                    .map(LogEntry::getIp)
                    .collect(Collectors.toSet());
            }

    @Override
    public Set<String> getIPsForEvent(Event event, Date after, Date before) {

            return logList
                    .stream()
                    .filter(entry -> dateBetweenDates(entry.getDate(), after, before) && entry.getEvent() == event)
                    .map(LogEntry::getIp)
                    .collect(Collectors.toSet());

    }

    @Override
    public Set<String> getIPsForStatus(Status status, Date after, Date before) {

            return logList
                    .stream()
                    .filter(entry -> dateBetweenDates(entry.getDate(), after, before) && entry.getStatus() == status)
                    .map(LogEntry::getIp)
                    .collect(Collectors.toSet());

    }

    @Override
    public Set<String> getAllUsers() {

        return logList
                .stream()
                .map(LogEntry::getUser)
                .collect(Collectors.toSet());
    }

    @Override
    public int getNumberOfUsers(Date after, Date before) {

            return logList
                    .stream()
                    .filter(entry -> dateBetweenDates(entry.getDate(), after, before))
                    .map(LogEntry::getUser)
                    .collect(Collectors.toSet()).size();

    }

    @Override
    public int getNumberOfUserEvents(String user, Date after, Date before) {

            return logList
                    .stream()
                    .filter(entry -> dateBetweenDates(entry.getDate(), after, before))
                    .map(LogEntry::getEvent)
                    .collect(Collectors.toSet()).size();
    }

    @Override
    public Set<String> getUsersForIP(String ip, Date after, Date before) {

            return logList
                    .stream()
                    .filter(entry -> dateBetweenDates(entry.getDate(), after, before) && entry.getIp().equals(ip))
                    .map(LogEntry::getUser)
                    .collect(Collectors.toSet());

    }

    @Override
    public Set<String> getLoggedUsers(Date after, Date before) {

            return logList
                    .stream()
                    .filter(entry -> dateBetweenDates(entry.getDate(), after, before) && entry.getEvent() == Event.LOGIN)
                    .map(LogEntry::getUser)
                    .collect(Collectors.toSet());
    }

    @Override
    public Set<String> getDownloadedPluginUsers(Date after, Date before) {

            return logList
                    .stream()
                    .filter(entry -> dateBetweenDates(entry.getDate(), after, before) && entry.getEvent() == Event.DOWNLOAD_PLUGIN)
                    .map(LogEntry::getUser)
                    .collect(Collectors.toSet());

    }

    @Override
    public Set<String> getWroteMessageUsers(Date after, Date before) {

            return logList
                    .stream()
                    .filter(entry -> dateBetweenDates(entry.getDate(), after, before) && entry.getEvent() == Event.WRITE_MESSAGE)
                    .map(LogEntry::getUser)
                    .collect(Collectors.toSet());
    }

    @Override
    public Set<String> getSolvedTaskUsers(Date after, Date before) {

            return logList
                    .stream()
                    .filter(entry -> dateBetweenDates(entry.getDate(), after, before) && entry.getEvent() == Event.SOLVE_TASK)
                    .map(LogEntry::getUser)
                    .collect(Collectors.toSet());
    }

    @Override
    public Set<String> getSolvedTaskUsers(Date after, Date before, int task) {

            return logList
                    .stream()
                    .filter(entry -> dateBetweenDates(entry.getDate(), after, before) && entry.getEvent() == Event.SOLVE_TASK && entry.getEventNumber() == task)
                    .map(LogEntry::getUser)
                    .collect(Collectors.toSet());
    }

    @Override
    public Set<String> getDoneTaskUsers(Date after, Date before) {

            return logList
                    .stream()
                    .filter(entry -> dateBetweenDates(entry.getDate(), after, before) && entry.getEvent() == Event.DONE_TASK)
                    .map(LogEntry::getUser)
                    .collect(Collectors.toSet());
    }

    @Override
    public Set<String> getDoneTaskUsers(Date after, Date before, int task) {

            return logList
                    .stream()
                    .filter(entry -> dateBetweenDates(entry.getDate(), after, before) && entry.getEvent() == Event.DONE_TASK && entry.getEventNumber() == task)
                    .map(LogEntry::getUser)
                    .collect(Collectors.toSet());
    }

    @Override
    public Set<Date> getDatesForUserAndEvent(String user, Event event, Date after, Date before) {

        return logList
                .stream()
                .filter(entry -> dateBetweenDates(entry.getDate(), after, before) && entry.getEvent() == event && entry.getUser().equals(user))
                .map(LogEntry::getDate)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Date> getDatesWhenSomethingFailed(Date after, Date before) {

        return logList
                .stream()
                .filter(entry -> dateBetweenDates(entry.getDate(), after, before) && entry.getStatus() == Status.FAILED)
                .map(LogEntry::getDate)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Date> getDatesWhenErrorHappened(Date after, Date before) {

        return logList
                .stream()
                .filter(entry -> dateBetweenDates(entry.getDate(), after, before) && entry.getStatus() == Status.ERROR)
                .map(LogEntry::getDate)
                .collect(Collectors.toSet());
    }

    @Override
    public Date getDateWhenUserLoggedFirstTime(String user, Date after, Date before) {

        return logList
                .stream()
                .filter(entry -> dateBetweenDates(entry.getDate(), after, before) && entry.getUser().equals(user) && entry.getEvent() == Event.LOGIN)
                .map(LogEntry::getDate)
                .min(Date::compareTo)
                .orElse(null);

    }

    @Override
    public Date getDateWhenUserSolvedTask(String user, int task, Date after, Date before) {

        return logList
                .stream()
                .filter(entry -> dateBetweenDates(entry.getDate(), after, before) && entry.getUser().equals(user) && entry.getEvent() == Event.SOLVE_TASK && entry.getEventNumber() == task)
                .map(LogEntry::getDate)
                .min(Date::compareTo)
                .orElse(null);
    }

    @Override
    public Date getDateWhenUserDoneTask(String user, int task, Date after, Date before) {

        return logList
                .stream()
                .filter(entry -> dateBetweenDates(entry.getDate(), after, before) && entry.getUser().equals(user) && entry.getEvent() == Event.DONE_TASK && entry.getEventNumber() == task)
                .map(LogEntry::getDate)
                .min(Date::compareTo)
                .orElse(null);
    }

    @Override
    public Set<Date> getDatesWhenUserWroteMessage(String user, Date after, Date before) {

        return logList
                .stream()
                .filter(entry -> dateBetweenDates(entry.getDate(), after, before) && entry.getUser().equals(user) && entry.getEvent() == Event.WRITE_MESSAGE)
                .map(LogEntry::getDate)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Date> getDatesWhenUserDownloadedPlugin(String user, Date after, Date before) {

        return logList
                .stream()
                .filter(entry -> dateBetweenDates(entry.getDate(), after, before) && entry.getUser().equals(user) && entry.getEvent() == Event.DOWNLOAD_PLUGIN)
                .map(LogEntry::getDate)
                .collect(Collectors.toSet());
    }

    @Override
    public int getNumberOfAllEvents(Date after, Date before) {

        return (int) logList
                .stream()
                .filter(entry -> dateBetweenDates(entry.getDate(), after, before))
                .map(LogEntry::getEvent)
                .distinct()
                .count();
    }

    @Override
    public Set<Event> getAllEvents(Date after, Date before) {

        return logList
                .stream()
                .filter(entry -> dateBetweenDates(entry.getDate(), after, before))
                .map(LogEntry::getEvent)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Event> getEventsForIP(String ip, Date after, Date before) {

        return logList
                .stream()
                .filter(entry -> dateBetweenDates(entry.getDate(), after, before) && entry.getIp().equals(ip))
                .map(LogEntry::getEvent)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Event> getEventsForUser(String user, Date after, Date before) {

        return logList
                .stream()
                .filter(entry -> dateBetweenDates(entry.getDate(), after, before) && entry.getUser().equals(user))
                .map(LogEntry::getEvent)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Event> getFailedEvents(Date after, Date before) {

        return logList
                .stream()
                .filter(entry -> dateBetweenDates(entry.getDate(), after, before) && entry.getStatus() == Status.FAILED)
                .map(LogEntry::getEvent)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Event> getErrorEvents(Date after, Date before) {

        return logList
                .stream()
                .filter(entry -> dateBetweenDates(entry.getDate(), after, before) && entry.getStatus() == Status.ERROR)
                .map(LogEntry::getEvent)
                .collect(Collectors.toSet());
    }

    @Override
    public int getNumberOfAttemptToSolveTask(int task, Date after, Date before) {

        return (int) logList
                .stream()
                .filter(entry -> dateBetweenDates(entry.getDate(), after, before)
                        && entry.getEvent() == Event.SOLVE_TASK
                        && entry.getEventNumber() == task)
                .count();
    }

    @Override
    public int getNumberOfSuccessfulAttemptToSolveTask(int task, Date after, Date before) {

        return (int) logList
                .stream()
                .filter(entry -> dateBetweenDates(entry.getDate(), after, before)
                        && entry.getEvent() == Event.DONE_TASK
                        && entry.getEventNumber() == task)
                .count();
    }


    @Override
    public Map<Integer, Integer> getAllSolvedTasksAndTheirNumber(Date after, Date before) {
        return logList
                .stream()
                .filter(entry -> dateBetweenDates(entry.getDate(), after, before)
                        && entry.getEvent() == Event.SOLVE_TASK)
                .map(LogEntry::getEventNumber)
                .distinct()
                .collect(Collectors.toMap(k -> k, v -> getNumberOfAttemptToSolveTask(v, after, before)));
    }

    @Override
    public Map<Integer, Integer> getAllDoneTasksAndTheirNumber(Date after, Date before) {

        return logList
                .stream()
                .filter(entry -> dateBetweenDates(entry.getDate(), after, before)
                        && entry.getEvent() == Event.DONE_TASK)
                .map(LogEntry::getEventNumber)
                .distinct()
                .collect(Collectors.toMap(k -> k, v -> getNumberOfSuccessfulAttemptToSolveTask(v, after, before)));

    }



    @Override
    public Set<Object> execute(String query) {

        String field1;
        String field2 = null;
        String value1 = null;
        Date after = null;
        Date before = null;
        Pattern pattern = Pattern.compile("get (ip|user|date|event|status)"
                + "( for (ip|user|date|event|status) = \"(.*?)\")?"
                + "( and date between \"(.*?)\" and \"(.*?)\")?");
        Matcher matcher = pattern.matcher(query);
        matcher.find();
        field1 = matcher.group(1);
        if (matcher.group(2) != null) {
            field2 = matcher.group(3);
            value1 = matcher.group(4);
            if (matcher.group(5) != null) {
                after = dateParser(matcher.group(6));
                before = dateParser(matcher.group(7));
            }
        }

            String finalField = field2;
            String finalValue = value1;
            Date finalAfter = after;
            Date finalBefore = before;
            return logList
                    .stream()
                    .filter(entry -> equalsValues(entry, finalField, finalValue) && dateBetweenDates(entry.getDate(), finalAfter, finalBefore))
                    .map(entry -> getCurrentValue(entry, field1))
                    .collect(Collectors.toSet());
    }

   public static class LogEntry {

       private String ip;
       private String user;
       private Date date;
       private Event event;
       private Integer eventNumber;
       private Status status;

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Event getEvent() {
            return event;
        }

        public void setEvent(Event event) {
            this.event = event;
        }

        public Integer getEventNumber() {
            return eventNumber;
        }

        public void setEventNumber(Integer eventNumber) {
            this.eventNumber = eventNumber;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }


    }

    private abstract class Command {
        protected LogEntry logEntry;

        abstract Object execute();
    }

    private class GetIpCommand extends Command {
        public GetIpCommand(LogEntry logEntry) {
            this.logEntry = logEntry;
        }

        @Override
        Object execute() {
            return logEntry.getIp();
        }
    }

    private class GetUserCommand extends Command {
        public GetUserCommand(LogEntry logEntry) {
            this.logEntry = logEntry;
        }

        @Override
        Object execute() {
            return logEntry.getUser();
        }
    }

    private class GetDateCommand extends Command {
        public GetDateCommand(LogEntry logEntry) {
            this.logEntry = logEntry;
        }

        @Override
        Object execute() {
            return logEntry.getDate();
        }
    }

    private class GetEventCommand extends Command {
        public GetEventCommand(LogEntry logEntry) {
            this.logEntry = logEntry;
        }

        @Override
        Object execute() {
            return logEntry.getEvent();
        }
    }

    private class GetStatusCommand extends Command {
        public GetStatusCommand(LogEntry logEntry) {
            this.logEntry = logEntry;
        }

        @Override
        Object execute() {
            return logEntry.getStatus();
        }
    }
}





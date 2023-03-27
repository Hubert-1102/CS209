import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * This is just a demo for you, please run it on JDK17
 * (some statements may be not allowed in lower version).
 * This is just a demo, and you can extend and implement functions
 * based on this demo, or implement it in a different way.
 */
public class OnlineCoursesAnalyzer {

    List<Course> courses = new ArrayList<>();
    /**
     * This is a method description that is long enough to exceed right margin.
     * Line with manual
     * line feed.
     *
     */
    public OnlineCoursesAnalyzer(String datasetPath) {
        BufferedReader br = null;
        String line;
        try {
            br = new BufferedReader(new FileReader(datasetPath, StandardCharsets.UTF_8));
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] info = line.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)", -1);
                Course course = new Course(info[0], info[1], new Date(info[2]),
                        info[3], info[4], info[5],
                        Integer.parseInt(info[6]),
                        Integer.parseInt(info[7]),
                        Integer.parseInt(info[8]),
                        Integer.parseInt(info[9]),
                        Integer.parseInt(info[10]),
                        Double.parseDouble(info[11]),
                        Double.parseDouble(info[12]),
                        Double.parseDouble(info[13]),
                        Double.parseDouble(info[14]),
                        Double.parseDouble(info[15]),
                        Double.parseDouble(info[16]),
                        Double.parseDouble(info[17]),
                        Double.parseDouble(info[18]),
                        Double.parseDouble(info[19]),
                        Double.parseDouble(info[20]),
                        Double.parseDouble(info[21]),
                        Double.parseDouble(info[22]));
                courses.add(course);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //1
    /**
     * This is a method description that is long enough to exceed right margin.
     * Line with manual
     * line feed.
     */
    public Map<String, Integer> getPtcpCountByInst() {
        Map<String, Integer> map = new LinkedHashMap<>();
        List<Course> courses = this.courses;
        courses.stream().sorted(Comparator.comparing(a -> a.institution)).forEach(c -> {
            if (map.containsKey(c.institution)) {
                int count = map.get(c.institution);
                count += c.participants;
                map.put(c.institution, count);
            } else {
                map.put(c.institution, c.participants);
            }
        });
        return map;
    }

    //2
    /**
     * This is a method description that is long enough to exceed right margin.
     * Line with manual
     * line feed.
     */
    public Map<String, Integer> getPtcpCountByInstAndSubject() {
        Map<String, Integer> map = new HashMap<>();
        List<Course> list = this.courses;
        list.stream().sorted(Comparator.comparing(a -> a.institution + '-' + a.subject)).forEach(course -> {
            if (map.containsKey(course.institution + '-' + course.subject)) {
                int count = map.get(course.institution + '-' + course.subject);
                count += course.participants;
                map.put(course.institution + '-' + course.subject, count);
            } else {
                map.put(course.institution + '-' + course.subject, course.participants);
            }
        });
        Map<String, Integer> result = new LinkedHashMap<>();
        map.entrySet().stream().sorted((a, b) -> b.getValue() - a.getValue()).forEach(stringIntegerEntry -> {
            result.put(stringIntegerEntry.getKey(), stringIntegerEntry.getValue());
        });
        return result;
    }

    //3
    /**
     * This is a method description that is long enough to exceed right margin.
     * Line with manual
     * line feed.
     */
    public Map<String, List<List<String>>> getCourseListOfInstructor() {
        Map<String, List<List<String>>> map = new HashMap<>();
        this.courses.forEach(course -> {
            String[] teachers = course.instructors.split(",");
            if (teachers[0].equals("Anant Agarwal"))
                System.out.println(1);
            if (teachers.length == 1) {
                getCourseListOfInstructorHelp(map, teachers[0], course.title, 0);
            } else {
                for (String teacher : teachers) {
                    getCourseListOfInstructorHelp(map, teacher, course.title, 1);
                }
            }
        });
        map.entrySet().forEach(stringListEntry -> {
            List<String> list1 = stringListEntry.getValue().get(0).stream().sorted().toList();
            List<String> list2 = stringListEntry.getValue().get(1).stream().sorted().toList();
            List<List<String>> list = new ArrayList<>();
            list.add(list1);
            list.add(list2);
            stringListEntry.setValue(list);
        });
        return map;
    }
    /**
     * This is a method description that is long enough to exceed right margin.
     * Line with manual
     * line feed.
     */
    public static void getCourseListOfInstructorHelp(Map<String, List<List<String>>> map, String teacher, String title, int index) {
        teacher = teacher.strip();
        if (map.containsKey(teacher)) {
            if (!map.get(teacher).get(index).contains(title)) {
                List<List<String>> list = map.get(teacher);
                list.get(index).add(title);
                map.put(teacher, list);
            }
        } else {
            List<List<String>> list = new ArrayList<>();
            list.add(new ArrayList<>());
            list.add(new ArrayList<>());
            list.get(index).add(title);
            map.put(teacher, list);
        }
    }

    //4
    /**
     * This is a method description that is long enough to exceed right margin.
     * Line with manual
     * line feed.
     */
    public List<String> getCourses(int topK, String by) {
        List<String> list = new ArrayList<>();
        HashSet<String> set = new HashSet<>();
        Comparator<Course> comparator = Comparator.comparing(a -> a.title);
        Comparator<Course> comparatorInt = Comparator.comparing(a -> -a.participants);
        Comparator<Course> comparatorDouble = Comparator.comparing(a -> -a.totalHours);
        Comparator<Course> comparator1 = comparatorInt.thenComparing(comparator);
        Comparator<Course> comparator2 = comparatorDouble.thenComparing(comparator);
        if (by.equals("participants")) {
            list = this.courses.stream().sorted(comparator1).filter(a -> set.add(a.title)).limit(topK).map(a -> a.title
            ).toList();
        } else if (by.equals("hours")) {
            list = this.courses.stream().sorted(comparator2).filter(a -> set.add(a.title)).limit(topK).map(a -> a.title
            ).toList();
        } else {
            System.out.println("not allowed");
        }
        return list;
    }

    //5
    /**
     * This is a method description that is long enough to exceed right margin.
     * Line with manual
     * line feed.
     */
    public List<String> searchCourses(String courseSubject, double percentAudited, double totalCourseHours) {
        HashSet<String> set = new HashSet<>();

        return this.courses.stream().
                filter(a -> a.subject.toLowerCase().contains(courseSubject.toLowerCase()) &&
                        a.percentAudited >= percentAudited &&
                        a.totalHours <= totalCourseHours &&
                        set.add(a.title)).
                map(a -> a.title).sorted().toList();
    }

    //6
    /**
     * This is a method description that is long enough to exceed right margin.
     * Line with manual
     * line feed.
     */
    public List<String> recommendCourses(int age, int gender, int isBachelorOrHigher) {
        List<String> list;
        Map<String, Q6> map = new HashMap<>();
        this.courses.forEach(course -> {
            if (map.containsKey(course.number)) {
                Q6 q6 = map.get(course.number);
                q6.age = (q6.age * q6.count + course.medianAge) / (q6.count + 1);
                q6.degree = (q6.degree * q6.count + course.percentDegree) / (q6.count + 1);
                q6.male = (q6.male * q6.count + course.percentMale) / (q6.count + 1);
                q6.count += 1;
                if (course.launchDate.compareTo(q6.date) > 0) {
                    q6.title = course.title;
                }
                map.put(course.number, q6);
            } else {
                map.put(course.number, new Q6(course.percentMale, course.medianAge, course.percentDegree, 1, course.title, course.launchDate));
            }
        });
        Comparator<Q6> comparator1 = Comparator.comparingDouble(a -> (Math.pow(age - a.age, 2) + Math.pow(gender * 100 - a.male, 2) + Math.pow(isBachelorOrHigher * 100
                - a.degree, 2)));
        Comparator<Q6> comparator2 = Comparator.comparing(q6 -> q6.title);
        Comparator<Q6> comparator = comparator1.thenComparing(comparator2);
        list = map.values().stream().sorted(comparator).map(a -> a.title).distinct().limit(10).toList();
        return list;
    }

    private static class Q6 {
        double male;
        double age;
        double degree;
        int count;
        String title;
        Date date;

        public Q6(double male, double age, double degree, int count, String title, Date date) {
            this.male = male;
            this.age = age;
            this.degree = degree;
            this.count = count;
            this.title = title;
            this.date = date;
        }
    }

}

class Course {
    String institution;
    String number;
    Date launchDate;
    String title;
    String instructors;
    String subject;
    int year;
    int honorCode;
    int participants;
    int audited;
    int certified;
    double percentAudited;
    double percentCertified;
    double percentCertified50;
    double percentVideo;
    double percentForum;
    double gradeHigherZero;
    double totalHours;
    double medianHoursCertification;
    double medianAge;
    double percentMale;
    double percentFemale;
    double percentDegree;

    public Course(String institution, String number, Date launchDate,
                  String title, String instructors, String subject,
                  int year, int honorCode, int participants,
                  int audited, int certified, double percentAudited,
                  double percentCertified, double percentCertified50,
                  double percentVideo, double percentForum, double gradeHigherZero,
                  double totalHours, double medianHoursCertification,
                  double medianAge, double percentMale, double percentFemale,
                  double percentDegree) {
        this.institution = institution;
        this.number = number;
        this.launchDate = launchDate;
        if (title.startsWith("\"")) title = title.substring(1);
        if (title.endsWith("\"")) title = title.substring(0, title.length() - 1);
        this.title = title;
        if (instructors.startsWith("\"")) instructors = instructors.substring(1);
        if (instructors.endsWith("\"")) instructors = instructors.substring(0, instructors.length() - 1);
        this.instructors = instructors;
        if (subject.startsWith("\"")) subject = subject.substring(1);
        if (subject.endsWith("\"")) subject = subject.substring(0, subject.length() - 1);
        this.subject = subject;
        this.year = year;
        this.honorCode = honorCode;
        this.participants = participants;
        this.audited = audited;
        this.certified = certified;
        this.percentAudited = percentAudited;
        this.percentCertified = percentCertified;
        this.percentCertified50 = percentCertified50;
        this.percentVideo = percentVideo;
        this.percentForum = percentForum;
        this.gradeHigherZero = gradeHigherZero;
        this.totalHours = totalHours;
        this.medianHoursCertification = medianHoursCertification;
        this.medianAge = medianAge;
        this.percentMale = percentMale;
        this.percentFemale = percentFemale;
        this.percentDegree = percentDegree;
    }
}

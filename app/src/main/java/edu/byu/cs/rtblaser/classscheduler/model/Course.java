package edu.byu.cs.rtblaser.classscheduler.model;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Course implements Comparable<Course> {

	private List<Course> extent = new ArrayList<>();

	public Course() {
		
	}
	
	public Course(List<Section> s) {
		sections = s;
	}

	public Course(String department, String catalogNumber, String curriculumId, String titleCode) {
		this.department = department;
		this.catalogNumber = catalogNumber;
		this.curriculumId = curriculumId;
		this.titleCode = titleCode;
		this.courseTitle = department + " " + catalogNumber;
		sections = new ArrayList<>();
	}

	String department;
	String courseTitle;
	String catalogNumber; // i.e. C S '340'
	String curriculumId; // Used by BYU api to locate sections
	String titleCode; // Used by BYU api to locate sections
	Course[] prerequisites;
	List<Section> sections;
	String description;
	String duration; // Semester long, first block, second block
	char whenTaught; // Fall 5, Winter 1, Spring 3, Summer 4
	double creditHours;
	
	public void getCoursesFromApi() {
		Time time = new Time(9,0,0);
		Time time2 = new Time(10,0,0);
		if(time.after(time2)) {
			return;
		}
	}

	public Course removeSectionsThatConflictWithConflicts(Map<String, List<Conflict>> conflicts) {
		Set<Integer> sectionIndexesToRemove = new TreeSet<>();
		for (int i = 0; i < sections.size(); i++) {
			Section section = sections.get(i);
			Set<String> days = section.dayToTime.keySet();
			if (days != null) {
				Iterator<String> iter = days.iterator();
				while(iter.hasNext()) {
					String day = iter.next();
					List<Time[]> times = section.dayToTime.get(day);
					if (times != null) {
						for (Time[] startEnd : times) {
							Time startTime = startEnd[0];
							Time endTime = startEnd[1];
							List<Conflict> dayConflicts = conflicts.get(day);
							if (dayConflicts != null) {								
								for (Conflict conflict : dayConflicts) {
									Time conflictStart = conflict.getStartTime();
									Time conflictEnd = conflict.getEndTime();
									boolean conflictsWithStart = (conflictStart.after(startTime) && conflictStart.before(endTime))
											|| conflictStart.equals(startTime) || conflictStart.equals(endTime);
									boolean conflictsWithEnd = (conflictEnd.after(startTime) && conflictEnd.before(endTime))
											|| conflictEnd.equals(startTime) || conflictEnd.equals(endTime);
									boolean edgeCases = conflictStart.before(startTime) && conflictEnd.after(endTime);
									if (conflictsWithStart || conflictsWithEnd || edgeCases) {
										sectionIndexesToRemove.add(i);
									}
								}
							}
						}
					}
				}
			}
		}
		int offset = 0;
		for (int index : sectionIndexesToRemove) {
			sections.remove(index - offset++);
		}
		return this;
	}
	
	public Course sortSectionsByPreference(Preference preferences) {
		for(Section section : sections) {
            section = section.weightByPreferences(preferences);
        }
        Collections.sort(sections);
        return this;
	}
	
	@Override
	public int compareTo(Course compareCourse) {
		int thisSectionsSize = this.sections.size();
		int compareCourseSectionsSize = compareCourse.sections.size();
		if(thisSectionsSize > compareCourseSectionsSize) {
			return -1;
		} else if (thisSectionsSize < compareCourseSectionsSize) {
			return 1;
		}
		return 0;
	}

	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	public String getCatalogNumber() {
		return catalogNumber;
	}

	public void setCatalogNumber(String catalogNumber) {
		this.catalogNumber = catalogNumber;
	}

	public String getCurriculumId() {
		return curriculumId;
	}

	public void setCurriculumId(String curriculumId) {
		this.curriculumId = curriculumId;
	}

	public String getTitleCode() {
		return titleCode;
	}

	public void setTitleCode(String titleCode) {
		this.titleCode = titleCode;
	}

	public Course[] getPrerequisites() {
		return prerequisites;
	}

	public void setPrerequisites(Course[] prerequisites) {
		this.prerequisites = prerequisites;
	}

	public List<Section> getSections() {
		return sections;
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public char getWhenTaught() {
		return whenTaught;
	}

	public void setWhenTaught(char whenTaught) {
		this.whenTaught = whenTaught;
	}

	public double getCreditHours() {
		return creditHours;
	}

	public void setCreditHours(double creditHours) {
		this.creditHours = creditHours;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public void addSection(Section s) {
		sections.add(s);
	}
	
}

//{
//    "curriculum_id": "10116",
//    "title_code": "000",
//    "catalog_number": "100",
//    "course_title": "Fund of Computing"
//  }
package edu.byu.cs.rtblaser.classscheduler.model;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scheduler {
	private Scheduler() {
		
	}

	//TODO: Add functionality for which sections conflict

	public static Scheduler SINGLETON = new Scheduler();
	
	Schedule mySchedule = new Schedule();
	
	public Schedule makeSchedule(List<Course> courses, Map<String, List<Conflict>> conflicts, Preference preferences) throws AllSectionsConflictWithConflictsException, NoPossibleScheduleException {
		if (mySchedule.getClasses().size() > 0) {
            mySchedule.getClasses().clear();
        }
        for (Course course : courses) {
			course.removeSectionsThatConflictWithConflicts(conflicts);
			if (course.sections.size() == 0) {
				throw (new AllSectionsConflictWithConflictsException(course.courseTitle));
			} else {
				course = course.sortSectionsByPreference(preferences);
			}
		}
		sortByNumberOfSections(courses);
		int courseIndex = 0;
		int sectionIndex = 0;
		int highestCourseSoFar = 0;
		Map<Integer, Integer> courseToSection = new HashMap<>();
		Course theCourse = courses.get(0);
		while (courseToSection.size() < 1) {
			if (sectionIndex < theCourse.getSections().size()) {
				if (theCourse.getSections().get(sectionIndex).isAvailable()) {
					addSectionToSchedule(courses, courseToSection, courseIndex, sectionIndex);
				} else {
					sectionIndex++;
				}
			} else {
				mySchedule.getClasses().clear();
				for (Course daCourse : courses) {
					daCourse.getSections().clear();
				}
				throw new NoPossibleScheduleException(courses.get(highestCourseSoFar).courseTitle);
			}
		}
		courseIndex++;
		while (courseIndex < courses.size()) {
			if (courseIndex == -1) {
				mySchedule.getClasses().clear();
				for (Course daCourse : courses) {
					daCourse.getSections().clear();
				}
                throw new NoPossibleScheduleException(courses.get(highestCourseSoFar).courseTitle);
            }
            if (courseIndex > highestCourseSoFar) {
				highestCourseSoFar = courseIndex;
			}
            Course course = courses.get(courseIndex);
            if (!courseToSection.containsKey(courseIndex)) {
                courseToSection.put(courseIndex, -1);
            }
			boolean added = false;
			for (sectionIndex = courseToSection.get(courseIndex) + 1; sectionIndex < course.sections.size(); sectionIndex++) {
				Section section = course.sections.get(sectionIndex);
				if (mySchedule.getClasses().size() > 0) {
					for (Section scheduledSection : mySchedule.classes) {
						added = (!section.conflictsWithScheduledSection(scheduledSection) && section.isAvailable());
						if (!added) {
							break;
						}
					}
				} else {
					added = section.isAvailable();
				}
				if (added) {
					addSectionToSchedule(courses, courseToSection, courseIndex, sectionIndex);
					courseIndex++;
					break;
				}
			}
			if (!added) {
                courseToSection.remove(courseIndex--);
			}
		}
		
		return mySchedule;
	}
	
	public List<Course>sortByNumberOfSections(List<Course> courses) {
		Collections.sort(courses);
		return courses;
	}
	
	public void addSectionToSchedule(List<Course> courses, Map<Integer, Integer> courseToSection, int courseIndex, int sectionIndex) {
		mySchedule.addSection(courses.get(courseIndex).sections.get(sectionIndex));
		if (courseToSection.containsKey(courseIndex)) {
			courseToSection.remove(courseIndex);
		}
		courseToSection.put(courseIndex, sectionIndex);
	}
	
	@SuppressWarnings("serial")
	public class AllSectionsConflictWithConflictsException extends Exception {
		String message;
		public AllSectionsConflictWithConflictsException(String courseTitle) {
			message = courseTitle + " does not have any sections that are available with your conflicts. Please select another course.";
		}

		@Override
		public String getMessage() {
			return message;
		}
	}

    @SuppressWarnings("serial")
    public class NoPossibleScheduleException extends Exception {
        String message;
        public NoPossibleScheduleException(String courseTitle) {
            message = "There is no possible schedule for the given courses. " + courseTitle + " could not " +
					"be added due to conflicts with other desired courses. Please consider revising your schedule.";
        }

        @Override
        public String getMessage() {
            return message;
        }
    }

	public Schedule getMySchedule() {
		return mySchedule;
	}

	public void setMySchedule(Schedule mySchedule) {
		this.mySchedule = mySchedule;
	}
	
	
}

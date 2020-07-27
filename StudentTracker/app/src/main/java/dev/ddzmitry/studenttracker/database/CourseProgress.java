package dev.ddzmitry.studenttracker.database;

/**
 * Created by dzmitrydubarau on 7/26/20.
 */

public enum CourseProgress {
    PLANNED {
        public String getStatus(){ return "Planned to take";};
    },
    COMPLETED {
        public String getStatus(){ return "Completed";};
    },
    IN_PROGRESS{
        public String getStatus(){ return "Dropped";};
    },
    DROPPED{
        public String getStatus(){ return "Planned to take";};
    }
}

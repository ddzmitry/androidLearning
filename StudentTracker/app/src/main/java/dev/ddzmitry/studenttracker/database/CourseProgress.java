package dev.ddzmitry.studenttracker.database;

/**
 * Created by dzmitrydubarau on 7/26/20.
 */

public enum CourseProgress {
    PLANNED {
        @Override
        public String toString(){ return "Planned to take";};
    },
    COMPLETED {
        @Override
        public String toString(){ return "Completed";};
    },
    IN_PROGRESS{
        @Override
        public String toString(){ return "In Progress";};
    },
    DROPPED{
        @Override
        public String toString(){ return "Dropped";};
    }
}

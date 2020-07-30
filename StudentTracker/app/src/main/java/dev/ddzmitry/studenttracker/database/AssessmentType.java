package dev.ddzmitry.studenttracker.database;

/**
 * Created by dzmitrydubarau on 7/26/20.
 */

public enum AssessmentType {
    PA {
        @Override
        public String toString(){ return "Performance Assessment";}
    },
    OA {
        @Override
        public String toString(){ return "Objective Assessment";}
    }
}

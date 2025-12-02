package com.smartplanner;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * Test Suite for Smart Planner Application
 * Runs all tests across all packages
 */
@Suite
@SuiteDisplayName("Smart Planner Test Suite")
@SelectPackages({
    "com.smartplanner.model",
    "com.smartplanner.service",
    "com.smartplanner.auth",
    "com.smartplanner.data",
    "com.smartplanner.utils",
    "com.smartplanner.validation"
})
public class SmartPlannerTestSuite {
  // This class remains empty, it is used only as a holder for the above
  // annotations
}

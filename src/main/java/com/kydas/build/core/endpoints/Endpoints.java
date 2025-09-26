package com.kydas.build.core.endpoints;

public class Endpoints {
    public static final String USERS_ENDPOINT = "/api/admin/users";
    public static final String EVENTS_ENDPOINT = "/api/admin/events";
    public static final String ORGANIZATIONS_ENDPOINT = "/api/admin/organizations";
    public static final String ACCOUNT_ENDPOINT = "/api/account";
    public static final String AUTH_ENDPOINT = "/api/auth";
    public static final String FILES_ENDPOINT = "/api/files";

    public static final String DICTIONARIES = "/api/dictionaries";
    public static final String CONSTRUCTION_VIOLATIONS = DICTIONARIES + "/construction-violations";
    public static final String CONSTRUCTION_WORKS = DICTIONARIES + "/construction-works";
    public static final String NORMATIVE_DOCUMENTS = DICTIONARIES + "/normative-documents";
    public static final String CONSTRUCTION_WORK_STAGES = CONSTRUCTION_WORKS + "/stages";

    public static final String PROJECTS = "/api/projects";
    public static final String PROJECTS_WORKS = PROJECTS + "/works";
    public static final String PROJECTS_WORKS_COMMENTS = PROJECTS_WORKS + "/comments";
    public static final String PROJECTS_VIOLATIONS = PROJECTS + "/violations";
    public static final String PROJECTS_VIOLATIONS_COMMENTS = PROJECTS_VIOLATIONS + "/comments";
    public static final String PROJECTS_VISITS = PROJECTS + "/visits";
}

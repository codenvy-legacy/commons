/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 *  [2012] - [2013] Codenvy, S.A.
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.commons.factory;

/** Class holds Factory url parameters values */
public class FactoryUrl {
    private String version;

    private String vcs;

    private String vcsUrl;

    private String commitId;

    private String projectName;

    private String workspaceName;

    public FactoryUrl() {
    }

    public FactoryUrl(String version, String vcs, String vcsUrl, String commitId, String projectName, String workspaceName) {
        this.version = version;
        this.vcs = vcs;
        this.vcsUrl = vcsUrl;
        this.commitId = commitId;
        this.projectName = projectName;
        this.workspaceName = workspaceName;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setVcs(String vcs) {
        this.vcs = vcs;
    }

    public void setVcsUrl(String vcsUrl) {
        this.vcsUrl = vcsUrl;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setWorkspaceName(String workspaceName) {
        this.workspaceName = workspaceName;
    }

    public String getVersion() {
        return version;
    }

    public String getVcs() {
        return vcs;
    }

    public String getVcsUrl() {
        return vcsUrl;
    }

    public String getCommitId() {
        return commitId;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getWorkspaceName() {
        return workspaceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FactoryUrl that = (FactoryUrl)o;

        if (commitId != null ? !commitId.equals(that.commitId) : that.commitId != null) return false;
        if (projectName != null ? !projectName.equals(that.projectName) : that.projectName != null) return false;
        if (vcs != null ? !vcs.equals(that.vcs) : that.vcs != null) return false;
        if (vcsUrl != null ? !vcsUrl.equals(that.vcsUrl) : that.vcsUrl != null) return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;
        if (workspaceName != null ? !workspaceName.equals(that.workspaceName) : that.workspaceName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = version != null ? version.hashCode() : 0;
        result = 31 * result + (vcs != null ? vcs.hashCode() : 0);
        result = 31 * result + (vcsUrl != null ? vcsUrl.hashCode() : 0);
        result = 31 * result + (commitId != null ? commitId.hashCode() : 0);
        result = 31 * result + (projectName != null ? projectName.hashCode() : 0);
        result = 31 * result + (workspaceName != null ? workspaceName.hashCode() : 0);
        return result;
    }
}

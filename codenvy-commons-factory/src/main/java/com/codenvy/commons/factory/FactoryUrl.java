/*
 * Copyright (C) 2013 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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

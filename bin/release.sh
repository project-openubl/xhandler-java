#!/usr/bin/env bash
#
# Copyright 2019 Project OpenUBL, Inc. and/or its affiliates
# and other contributors as indicated by the @author tags.
#
# Licensed under the Eclipse Public License - v 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# https://www.eclipse.org/legal/epl-2.0/
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#


echo "===================================="
read -p "Enter Release: " releaseVersion
read -p "Enter Next Release (SNAPSHOT): " nextVersion
read -p "Enter Github Username: " username
read -s -p "Enter Github Password: " password

# Update to release
mvn versions:set -DnewVersion="$releaseVersion"

git add .
git commit -m "Prepare release $releaseVersion"
git push "https://$username:$password@github.com/projectopenubl/xml-sender-lib.git"

# Create tag and push
git tag "$releaseVersion"
git push "https://$username:$password@github.com/projectopenubl/xml-sender-lib.git" --tags

# Create next snapshot
mvn versions:set -DnewVersion="$nextVersion-SNAPSHOT"

git add .
git commit -m "Prepare next release $nextVersion-SNAPSHOT"
git push "https://$username:$password@github.com/projectopenubl/xml-sender-lib.git"

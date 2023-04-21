# Core Forms

Core Forms is a simple and lean extension for the [CoreMedia CMS](https://www.coremedia.com/).

Build simple forms in the CoreMedia Studio by your needs using the most common form fields: 
- TextField
- TextArea
- NumberField
- SelectBox
- RadioButtons
- CheckBoxes
- FileUpload
- E-Mail-Field

Each form can be assigned to one formAction which processes the form requests in the cae backend.

Form-Actions and -fields can be customized and added by your need.


# Getting started

See the [releases](https://github.com/tallence/core-forms/releases) for tested compatibilities.
For earlier versions, have a look into these branches:
- v2010.2. See the branch [2010.2-compatible](https://github.com/tallence/core-forms/tree/2010.2-compatible).
- CoreMedia 9 (v19.04). See the branch: [1904.2-compatible](https://github.com/tallence/core-forms/tree/1904.2-compatible).
- Other CoreMedia 9 versions: It is in general compatible with the versions 17.10 and 18.10, but some small changes could be required (import paths, names of artifacts)

This repo covers the studio- and the backend-cae part. If you are looking for an example implementation for the frontend part (a Vue.js-App wrapped in a CoreMedia-Frontend-Workspace-Theme) have a look here: [core-forms-frontend](https://github.com/tallence/core-forms-frontend)

## Performance Fix
The Core-Forms Studio-Integration has a performance issue, related to the HideService. It registers a lot of Listeners, which won't be destroyed, so the studio will get slower and slower with each opened form-element.
The current workaround: Disable the HideService in the studio-server `application.properties`: `studio.hideservice.enabled=false`

## CM11 Getting started
The studio-frontend has been migrated into the new studio-pnpm-workspace structure. 
The form-editor-studio-plugin can still be part of this external extensions repo and does not need to be moved into `apps/studio-client/apps/main/extensions/`. But it needs to be registered as an extension with the extensions-tool: Call `mvn extensions:sync -Denable=core-forms` in the folder `workspace-configuration/extensions`   

## Integrate the Code in your CoreMedia Blueprint Workspace
You can integrate the extension in three ways. For all of them, you have to keep in mind:
* The formEditor uses maven-dependencies of the blueprint-workspace, keep the versions and groupIds in sync, e.g. "1-SNAPSHOT" and "com.coremedia.blueprint"
* After integrating the code, use the extension tool in the root folder of the project to link the modules into your workspace:
 ```
mvn -f workspace-configuration/extensions com.coremedia.maven:extensions-maven-plugin:LATEST:sync -Denable=core-forms
```


**1. Git SubModule**

Add this repo or your fork as a Git Submodule to your existing CoreMedia Blueprint-Workspace in the extensions-folder.
 
This way, you will be able to merge new commits made in this repo back to your fork.

Use this approach if you are planing to contribute changes to the GitHub-Repo.

From the project's root folder, clone this repository as submodule into the extensions folder. Make sure to use the branch name that matches your workspace version. 
```
git submodule add  https://github.com/tallence/core-forms.git modules/extensions/core-forms
```

**2. Git Subtree**

Add this repo or your fork as a Git Subtree to your existing CoreMedia Blueprint-Workspace in the extensions-folder.
Use this approach if you are using the GitHub-Repo in a readOnly mode.

To include the subtree into your project the following command line should be executed in the project root directory.

```git subtree add --prefix modules/extensions/core-forms https://github.com/tallence/core-forms.git master --squash```

Further information about git subtree in relation to the github repository can be found here:

[Github Subtree-Documentation](https://gist.github.com/SKempin/b7857a6ff6bddb05717cc17a44091202)

**3. Copy files**

Download the repo and copy the files into your Blueprint-Workspace Extension-Folder.

This way you won't be able to merge new commits made in this repo back to yours. So this way is not recommended.  
 

## Required modifications
To getting started you need to make some modifications and write some code:
      
**1. Changing the groupId and versionID**
The Formeditor is based on Artifacts from the CoreMedia Blueprint workspace. To make use of the existing dependency management of your Blueprint-Workspace it integrates in it by using the extensions-pom as a parent.
You need to change the groupId "com.coremedia.blueprint" and the version "1-SNAPSHOT" to those used in your workspace.
         
**2. Create Implementations for the Action-Adapters**
The FormEditor already contains two Actions: the DefaultAction and the MailAction. They are based on sending the form request data to at least one of the two adapters.
The module form-editor-cae contains the adapters as interfaces, you need to create implementations for them. 
Be aware of the 2 Void-Adapters, they were created to let you have a frustration-free first integration-experience with the formEditor: Your Adapter-Implementations need to use a Primary-Annotation. Otherwise, Spring will not be able to choose the right one for your project.  

* The [FormEditorMailAdapter](https://github.com/tallence/core-forms/blob/master/form-editor-cae/src/main/java/com/tallence/formeditor/cae/actions/FormEditorMailAdapter.java) is used to send mails to the user and the form admin. You might want to send the mails directly via JavaMailSender or via the CoreMedia elastic queue.
* The [FormEditorStorageAdapter](https://github.com/tallence/core-forms/blob/master/form-editor-cae/src/main/java/com/tallence/formeditor/cae/actions/FormEditorStorageAdapter.java) is used to store the form data in a storage of your choice, e.g. the elastic social mongoDB or a custom DB or CRM.

**3. Think about Security**
The FormController should be protected according to the required level of security. 
This could be:
* A ReCaptcha. This is an out-of-the-box feature in the FormEditor and can be activated by editors (BooleanProperty spamProtectionEnabled). but it is deactivated by default. To activate it by default remove the BooleanPropertyEditor in the Studio Form and modify the FormController (remove `if (target.isSpamProtectionEnabled()) {`). 
* By default a CSRF Token is required by the `org.springframework.security.web.csrf.CsrfFilter`. But you need to create one explicitly in your frontend. Otherwise the Post request will be rejected. If you do not want to use Csrf-Tokens, you cann add the property `cae.csrf.ignore-paths[1]=/servlet/dynamic/forms/**` to your CAEs.  

A Captcha is considered more secure than a Csrf-Token, also see [what the smart guys wrote about that.](http://www.owasp.org/index.php/Cross-Site_Request_Forgery_%28CSRF%29_Prevention_Cheat_Sheet). And it also protects from Spam.


That's it. Have fun ;) If you have any problems, questions, ideas, critics please contact us or create an issue. 

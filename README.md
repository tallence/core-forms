# Tallence Formeditor

The Tallence Formeditor is a simple and lean extension for the [CoreMedia CMS](https://www.coremedia.com/).

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

The extension is built for CoreMedia 9.17.10. It will probably be compatible with older CM9 versions and with newer versions but it is not tested yet.

This repo covers the studio- and the backend-cae part. If you are looking for an example implementation for the frontend part (ftl-Templates, css, js) have a look here: [formeditor-frontend](https://github.com/tallence/tallence-formeditor-frontend)

## Integrate the Code in your CoreMedia Blueprint Workspace
You can integrate the extension in three ways:

**1. Git SubModule**

Add this repo or your fork as a Git Submodule to your existing CoreMedia Blueprint-Workspace in the extensions-folder.
 
This way, you will be able to merge new commits made in this repo back to your fork.

This is the recommended approach because you will also be able to develop quickly, performing a make on the sources with a running studio- or cae-webapp.
 
**2. Copy files**

Download the repo and copy the files into your Blueprint-Workspace Extension-Folder.

This way you won't be able to merge new commits made in this repo back to yours. But if you do not like Git Submodules, you don't have to deal with them. 

**3. Own Workspace**

As mentioned by [Matthias Faust](https://github.com/mfaust) from CoreMedia on the 2018 DevCon, it will soon be possible to include external extensions into the Blueprint Workspace.
This would enable you to create a fork from this project and build it's jars independently from the Blueprint-Workspace.

However the formeditor uses maven-dependencies of the blueprint-workspace, so you have to keep the versions in sync.
    

## Required modifications
To getting started you need to make some modifications and write some code:
      
**1. Changing the groupId and versionID**
The Formeditor is based on Artifacts from the CoreMedia Blueprint workspace. To make use of the existing dependency management of your Blueprint-Workspace it integrates in it by using the extensions-pom as a parent.
You need to change the groupId "com.coremedia.blueprint" and the version "1-SNAPSHOT" to those used in your workspace.
         
**2. Create Implementations for the Action-Adapters**
The FormEditor already contains two Actions: the DefaultAction and the MailAction. They are based on sending the form request data to at least one of the two adapters.
The module form-editor-cae contains the adapters as interfaces, you need to create implementations for them. 

The com.tallence.formeditor.cae.actions.FormEditorMailAdapter is used to send mails to the user and the form admin. You might want to send the mails directly via JavaMailSender or via the CoreMedia elastic queue.
The com.tallence.formeditor.cae.actions.FormEditorStorageAdapter is used to store the form data in a storage of your choice, e.g. the elastic social mongoDB or a custom DB or CRM. 

That's it. Have fun ;) If you have any problems, questions, ideas, critics please contact us or create an issue. 
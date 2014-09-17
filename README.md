atombox-client
==============

Java 7 based directory watcher for storing image metadata in DB and backing up the files (e.g. uploading
to an Amazon S3 bucket). A 'dummy' uploader component is provided (and is default) for testing; it is easy to
provide new uploaders by implementing a new Uploader class and registering it in UploaderFactory.


Getting started
---------------
Copy or rename the file atombox.properties.example to atombox.properties and edit to your needs.


Database settings
---------------
In order to enable database integration (only MySQL tested), please run the SQL scripts in

        database/atombox.sql

and create a suitable user & password (with appropriate access rights). Database username & password can be
edited in atombox.properties.
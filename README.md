# properties-server

Dropwizard application that clones a git-repo and reads all .properties-files in it.
The properties are served on /property/{propertyName}.

Properties can be cleared by POST:ing to /tasks/clear.
Properties can be pulled and updated by POST:ing to /tasks/PullAndUpdate

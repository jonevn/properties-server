pipeline {
    
    agent any
    
    tools {
        maven 'maven'
        jdk 'jdk'
    }
    
    stages {
        stage('clean') {
        	steps {
            	sh 'mvn clean'
        	}
        }
        
        stage('compile') {
	    	steps {
	        	sh 'mvn compile'
	        }
        }
        
        stage('package') {
           	steps {
               	sh 'mvn package'
           	}
        }
        
        stage('install') {
           	steps {
               	sh 'mvn install'
           	}
        }
    }
}
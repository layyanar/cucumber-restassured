pipeline {
    agent any
    tools {
        maven 'Maven 3.8.1'
        jdk 'JDK 1.8'
    }

    stages {
        stage('Build') {
            steps {
                echo 'Building the application..'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing the application..'
                sh 'mvn clean test verify'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying the application....'
            }
        }
    }
}

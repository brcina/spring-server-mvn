pipeline {
    agent any

    tools {
        maven 'maven'
        git 'Default'
        jdk 'jdk21'
    }

    stages {
        stage('Prepare') {
            steps {
                echo 'Making mvnw executable...'
                sh 'chmod +x mvnw'
            }
        }
        stage('Build') {
            steps {
                echo 'Building..'
                sh './mvnw clean compile'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
                sh './mvnw clean test -Dspring.profiles.active=test'
            }
        }
        stage('Quality') {
            steps {
                withSonarQubeEnv('sonar-server') {
                    sh './mvnw sonar:sonar'
                }
            }
        }
        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }

    post {
        failure {
            mail to: 'boris.brcina@iso-gruppe.com',
                 subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
                 body: "Something is wrong with ${env.BUILD_URL}"
        }
    }
}
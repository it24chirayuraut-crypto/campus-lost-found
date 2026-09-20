pipeline {
    agent any

    tools {
        jdk 'JDK24'
        maven 'Maven3'
    }

    environment {
        DOCKERHUB_REPO = 'chiu009870/campus-lost-found'
        IMAGE_TAG = "${env.BUILD_NUMBER}"
        TEST_CONTAINER = "lostfound-test-${env.BUILD_NUMBER}"
    }

    triggers {
        pollSCM('H/5 * * * *')
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Unit Test (Maven)') {
            steps {
                bat 'mvn clean package'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                bat "docker build -t %DOCKERHUB_REPO%:%IMAGE_TAG% -t %DOCKERHUB_REPO%:latest ."
            }
        }

        stage('Run App Container') {
            steps {
                bat "docker run -d -p 8081:8081 --name %TEST_CONTAINER% %DOCKERHUB_REPO%:%IMAGE_TAG%"
                // give the app a few seconds to finish starting before tests hit it
                bat 'ping -n 10 127.0.0.1 > nul'
            }
        }

        stage('Selenium Test') {
            steps {
                bat 'mvn test -Dtest=LostFoundSeleniumTest'
            }
            post {
                always {
                    bat "docker stop %TEST_CONTAINER%"
                    bat "docker rm %TEST_CONTAINER%"
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    bat 'docker login -u %DOCKER_USER% -p %DOCKER_PASS%'
                    bat "docker push %DOCKERHUB_REPO%:%IMAGE_TAG%"
                    bat "docker push %DOCKERHUB_REPO%:latest"
                }
            }
        }

        stage('Deploy') {
            steps {
                bat 'docker stop lostfound-prod || exit 0'
                bat 'docker rm lostfound-prod || exit 0'
                bat "docker run -d -p 8081:8081 --name lostfound-prod %DOCKERHUB_REPO%:latest"
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully — app built, tested, and deployed.'
        }
        failure {
            echo 'Pipeline failed — check the stage logs above for details.'
        }
    }
}
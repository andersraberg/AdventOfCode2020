node {
    git 'https://github.com/andersraberg/AdventOfCode2020.git'
    stage('Build') {
        sh './gradlew clean build -Pversion=$BUILD_NUMBER --profile'
    }

    stage('Run') {
        sh './gradlew run'
    }

    stage('Code coverage') {
        sh './gradlew jacocoTestReport -Pversion=$BUILD_NUMBER'
        jacoco( 
            execPattern: 'build/jacoco/*.exec',
        )
    }

    stage('Sonar') {
        withSonarQubeEnv() {
            sh './gradlew sonarqube -Dsonar.projectKey=AdventofCode2020 -Pversion=$BUILD_NUMBER'
        }
    }

    stage('Report') {
        junit 'build/test-results/**/*.xml'
        sh 'mv build/reports/profile/*.html build/reports/profile/index.html'
        publishHTML([allowMissing: false,
                     alwaysLinkToLastBuild: false,
                     keepAll: true,
                     reportDir: 'build/reports/profile/',
                     reportFiles: 'index.html',
                     reportName: 'Gradle profile',
                     reportTitles: ''])
    }
    
}

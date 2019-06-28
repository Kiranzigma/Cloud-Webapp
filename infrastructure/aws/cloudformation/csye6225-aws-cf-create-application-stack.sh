#!/bin/bash
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Creating an APPLICATION stack with EC2 instance, Security Groups <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"

read -p "Enter the APPLICATION stack name to be created = " appname
appStackName="$appname-csye6225-app"

mapfile -t my_array < <( aws cloudformation list-stacks --stack-status-filter CREATE_COMPLETE | jq -r '.[] | .[] | .StackName' )
for i in "${my_array[@]}";
do
    if [ "$i" == "$appStackName" ] ; then
        printf "\n\n......................  Application Stack already exists, please use a different name ...................... \n\n"
        exit
    fi
done

read -p "Enter the NETWORK stack name to use = " networkName
netStackName="$networkName"

for i in "${my_array[@]}"; 
do
    if [ "$i" == "$netStackName" ] ; then
        read -p "Enter the SSH Key file name = " SSHKeyName

        read -p "Enter Your AMI ID: " AMI

        echo "...................... Creating $appStackName ......................"
        printf "......................  Please wait  ...................... \n"
        aws cloudformation create-stack --stack-name $appStackName --template-body file://csye6225-cf-application.json --capabilities CAPABILITY_NAMED_IAM --parameters ParameterKey=AppName,ParameterValue=$appStackName ParameterKey=NetworkName,ParameterValue=$netStackName ParameterKey=SSHKeyName,ParameterValue=$SSHKeyName  ParameterKey=AMI,ParameterValue=$AMI
        aws cloudformation wait stack-create-complete --stack-name $appStackName
            if [ $? -eq 0 ]; then
                printf "\n\n"
                printf "<<<<<<<<<<<<<<<<<<<<<<  Stack created successfully  >>>>>>>>>>>>>>>>>>>>>> \n\n"
                exit
            else
                printf "\n\n"
                printf "!!!!!!!!!!!!!!!!!!!!!!  Stack creation failed  !!!!!!!!!!!!!!!!!!!!!! \n"
                printf "\n\n"
                exit
            fi
            printf "\n"     
    fi       
done
        printf "\n\n................ Network Stack does not exists, please use a correct name  ................. \n\n"
        exit

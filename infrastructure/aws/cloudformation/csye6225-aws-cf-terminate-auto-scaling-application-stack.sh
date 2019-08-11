#!/bin/bash
echo "...................... Deleting an APPLICATION stack with EC2 instance, Security Groups ...................... "

read -p "Enter the APPLICATION stack name to be deleted = " appname
appStackName="$appname-csye6225-app"

mapfile -t my_array < <( aws cloudformation list-stacks --stack-status-filter CREATE_COMPLETE | jq -r '.[] | .[] | .StackName' )
for i in "${my_array[@]}";
do
	if [ "$i" == "$appStackName" ] ; then
        aws cloudformation delete-stack --stack-name $appStackName
		RESULT=$?
		printf "\n\n"
		printf "######################  Deleting the stack  ###################### \n\n"
		printf "......................  Please wait  ...................... \n"
		aws cloudformation wait stack-delete-complete --stack-name $appStackName
		if [ $RESULT -eq 0 ]; then
			printf "\n"
			printf "<<<<<<<<<<<<<<<<<<<<<<  Stack deleted successfully  >>>>>>>>>>>>>>>>>>>>>>"
		    printf "\n\n"
			exit
		else
			printf "\n\n"
			printf "!!!!!!!!!!!!!!!!!!!!!!  Stack deletion failed  !!!!!!!!!!!!!!!!!!!!!!"
			printf "\n\n"
			exit
		fi
    fi
done
    	printf "\n\n......................  Stack doesn't exists, please specify a correct name ...................... \n\n"
        exit  

#!/bin/bash
read -p "Enter Stack Name to Delete: " name
vpcName="$name-cc-vpc"

mapfile -t stackArray < <( aws cloudformation list-stacks --stack-status-filter CREATE_COMPLETE | jq -r '.[] | .[] | .StackName' )
for i in "${stackArray[@]}";
do
	if [ "$i" == "$name" ] ; then
        aws cloudformation delete-stack --stack-name $name
		RESULT=$?
		printf "\n\n"
		printf "########### Deleting the stack ################ \n\n"
		printf "......................  Please wait  ...................... \n"
		aws cloudformation wait stack-delete-complete --stack-name $name
		if [ $RESULT -eq 0 ]; then
			printf "\n"
			printf ".................  Deleted VPC '$vpcName' ................ \n\n"
			printf ".................  Deleted 3 Subnets  ...................... \n\n"
			printf ".................  Deleted InternetGateway  ...................... \n\n"
			printf ".............. ..  Deleted RouteTable  ...................... \n\n"
			printf ".................. Deleted Routes  ...................... \n\n"
			printf ".................. Deleted Routes  ...................... \n\n"
		    printf "<<<<<<<<<<<<<< Stack deleted successfully  >>>>>>>>>>>>>>"
		    printf "\n\n"
		else
			printf "\n\n"
			printf "!!!!!!!  Stack deletion failed !!!!!!!!!!!!!"
			printf "\n\n"
		fi
		printf "\n"
		exit
    else
    	printf "\n\n....... Stack with Name '$name' doesn't exists, please specify a correct name ..... \n\n"
        exit    
    fi
done


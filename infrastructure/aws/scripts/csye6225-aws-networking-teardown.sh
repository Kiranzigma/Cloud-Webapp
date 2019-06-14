#!/bin/bash
read -p "Enter your STACK_NAME to delete: " name
stack_name="$name-csye6225-vpc"

VPC_NAME="$name-MyVPC"

routetable="$name-routetable"
ig_const="$name-ig"
subnet="$name-subnet"


associate_id_array=($(aws ec2 describe-route-tables --filters Name="tag-value,Values=$routetable" --query [RouteTables[0].Associations[].RouteTableAssociationId] --output text))
echo $associate_id_array
if [ -z $associate_id_array ]; 
then
   echo "Error in Fetching the Array"
   exit
else
        for i in "${associate_id_array[@]}"
        do
                aws ec2 disassociate-route-table --association-id "$i"
done
fi
route_table_id=$(aws ec2 describe-route-tables --filters Name="tag-value,Values=$routetable" --query [RouteTables[0].RouteTableId] --output text)
echo $route_table_id
if [ -z $route_table_id ];
then
        echo 'Error in Deleting Route Table'
       exit 
else
        aws ec2 delete-route-table --route-table-id $route_table_id
        echo 'deleting route table successful'
fi
        declare -a subnet_id_array
        subnet_id_array=($(aws ec2 describe-subnets --filters Name="tag-value,Values=$subnet" --query [Subnets[].SubnetId] --output text))
        if [ -z $subnet_id_array ]; then
                                echo "Error in fetching subnet_array"
				exit
   else
        for i in "${subnet_id_array[@]}"
                                do
                                		echo "$i"
                                         aws ec2 delete-subnet --subnet-id "$i"
echo "Deleted subnet'$i'"
done
        vpc_id=$(aws ec2 describe-vpcs --filters Name="tag-value,Values=$VPC_NAME" --query [Vpcs[0].VpcId] --output text)
        if [ -z $vpc_id ]; then
                                            echo 'Error fetching VPC id.'
   exit

else
        echo $vpc_id

        gatewayId=$(aws ec2 describe-internet-gateways --filters Name="tag-value,Values=$ig_const" --query [InternetGateways[0].InternetGatewayId] --output text)
        aws ec2 detach-internet-gateway --internet-gateway-id $gatewayId --vpc-id $vpc_id
                                                aws ec2 delete-internet-gateway --internet-gateway-id $gatewayId
if [ $? -eq 0 ]; then
  echo "Deleting the Internet Gateway-> gateway id: "$gatewayId
  aws ec2 delete-vpc --vpc-id $vpc_id
                                            echo "Deleted VPC" 
                                            echo 'COMPLETED.'
  exit
else
   echo "Deleting The Internet Gateway Failed"
   
fi
                                            
                                    fi
                            fi

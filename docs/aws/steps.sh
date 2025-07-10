export AWS_PROFILE=local

aws ecs create-cluster --cluster-name local >/dev/null
aws ecs register-task-definition --cli-input-json file://ecs_taskdef.json >/dev/null

VPC_ID=$(aws ec2 create-vpc --cidr-block 10.0.0.0/16 --tag-specifications 'ResourceType=vpc,Tags=[{Key=Name,Value=local-vpc}]' --query 'Vpc.VpcId' --output text)
aws ec2 create-subnet --vpc-id $VPC_ID --cidr-block 10.0.1.0/24 --tag-specifications 'ResourceType=subnet,Tags=[{Key=Name,Value=local-subnet}]' --query 'Subnet.SubnetId' --output text | xclip -sel clip

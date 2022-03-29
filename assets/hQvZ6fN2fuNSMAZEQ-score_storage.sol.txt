pragma solidity >=0.4.22 <0.6.0;
contract score_storage {
    
    //mapping of time => score.
    //score is represented in integerform it needs to be between 10,000,000 and 0
    //so 9.45 would be 9450000. Precision of .6 digits is enought
    mapping(int => uint) data;
    //we keep a list of times to make the datamapping iterable externaly
    int[] times;
    
    //store the owner of this contract and data
    address owner;
    
    //mapping of addresses who are allowed to access data
    address[] allowed_users;
    
    //make sure only user can do certain things
    modifier user_only() {
        if (msg.sender == owner) {
            _;
        } else {
            revert();
        }
    }
    
    //make sure only allowed users can access data
    modifier allowed_users_only() {
        bool allowed = false;
        for(uint i = 0; i < allowed_users.length; i++) {
            if (allowed_users[i] == msg.sender) {
                allowed = true;
            }
        }
        if (allowed) {
            _;
        } else {
            revert();
        }
    }
    
    //set the owner
    constructor() public {
        owner = msg.sender;
        allowed_users.push(owner);
    }
    
    //add a score
    function add_score(int _time, uint _score) public user_only{
        //make sure no score is bigger than 10.000000
        require(_score < 10000000);
        //make sure there is only one score per time
        for(uint i = 0; i < times.length; i++) {
            require(times[i] != _time);
        }
        data[_time] = _score;
        times.push(_time);
    }
    
    //get all stored times
    function get_times() public view allowed_users_only returns (int[] memory){
        return times;
    }
    
    //access a score
    function get_score(int _time) public view allowed_users_only returns (uint){
        return data[_time];
    }
    
    //give access
    function give_access_right(address _allowed_user) public user_only {
        //make sure that the address is not already added
        bool add = true;
        for(uint i = 0; i < allowed_users.length; i++) {
            if(allowed_users[i] == _allowed_user) {
                add = false;
            }
        }
        //add the address
        if(add) {
            allowed_users.push(_allowed_user);    
        }
    }
    
    //remove access
    function remove_access_right(address _non_allowed_user) public user_only {
        //make sure that the owner cannot remove his own rights
        require(_non_allowed_user != owner);
        //remove the user
        for(uint i = 0; i < allowed_users.length; i++) {
            //find the user
            if(allowed_users[i] == _non_allowed_user) {
                //shift the array to fill the gap (thus deleting the i'th element)
                for(uint k = i; k < allowed_users.length - 1; k++) {
                    allowed_users[k] = allowed_users[k+1];
                }
                //adjust length of array
                allowed_users.length -= 1;
            }   
        }
    }
    
    //queue who has access
    function who_has_access_right() public view user_only returns (address[] memory) {
        return allowed_users;
    }
    
    //make everything in here inacessable 
    //this makes every function call fail in the future
    function delete_everyting() public user_only {
        //no one is allowed
        allowed_users.length = 0;
        //no one is the owner
        owner = address(0);
    }
}
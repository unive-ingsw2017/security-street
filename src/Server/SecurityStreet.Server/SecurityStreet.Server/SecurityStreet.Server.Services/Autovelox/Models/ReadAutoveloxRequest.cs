using System.Collections.Generic;
using ServiceStack;
using SecurityStreet.Server.Models.Dto;
using SecurityStreet.Server.Services.Models;

namespace SecurityStreet.Server.Services.Autovelox
{
    [Route("/autovelox/{Id}", "GET")]
    [Route("/autovelox", "GET")]
    public class ReadAutoveloxRequest : ReadRequest, IReturn<List<AutoveloxDto>> 
    {
    }
}
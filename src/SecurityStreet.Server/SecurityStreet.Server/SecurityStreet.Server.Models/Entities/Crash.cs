using SecurityStreet.Server.Models.Base;
using ServiceStack.DataAnnotations;
using System;


namespace SecurityStreet.Server.Models.Entities
{
    [Alias("Crashes")]
    public class Crash : BaseEntityWithAutoIncrement
    {
        /// <summary>
        /// Gets or sets the region.
        /// </summary>
        /// <value>
        /// The region.
        /// </value>
        public string Region { get; set; }

        /// <summary>
        /// Gets or sets the state.
        /// </summary>
        /// <value>
        /// The state.
        /// </value>
        public string State { get; set; }

        /// <summary>
        /// Gets or sets the total circulating.
        /// </summary>
        /// <value>
        /// The total circulating.
        /// </value>
        public int TotalCirculating { get; set; }

        /// <summary>
        /// Gets or sets the crashes.
        /// </summary>
        /// <value>
        /// The crashes.
        /// </value>
        public int Crashes { get; set; }

        /// <summary>
        /// Gets or sets the injuried.
        /// </summary>
        /// <value>
        /// The injuried.
        /// </value>
        public int Injuried { get; set; }

        /// <summary>
        /// Gets or sets the deadly crashes.
        /// </summary>
        /// <value>
        /// The deadly crashes.
        /// </value>
        public int DeadlyCrashes { get; set; }

        /// <summary>
        /// Gets or sets the men.
        /// </summary>
        /// <value>
        /// The men.
        /// </value>
        public int Men { get; set; }

        /// <summary>
        /// Gets or sets the females.
        /// </summary>
        /// <value>
        /// The females.
        /// </value>
        public int Females { get; set; }

        /// <summary>
        /// Gets or sets the index of the mortality.
        /// </summary>
        /// <value>
        /// The index of the mortality.
        /// </value>
        public int MortalityIndex { get; set; }

        /// <summary>
        /// Gets or sets the latitude.
        /// </summary>
        /// <value>
        /// The latitude.
        /// </value>
        public double Latitude { get; set; }

        /// <summary>
        /// Gets or sets the longitude.
        /// </summary>
        /// <value>
        /// The longitude.
        /// </value>
        public double Longitude { get; set; }
    }
}
